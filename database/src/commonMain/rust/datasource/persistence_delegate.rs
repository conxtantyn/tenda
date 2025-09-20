use libsql::{ Database };
use serde_json::{ json, Value as JsonValue };
use std::sync::{ Arc, Mutex };

use crate::datasource::persistence::Persistence;
use crate::mapper::entry_mapper::EntryMapper;
use crate::mapper::json_mapper::JsonMapper;
use crate::model::entry::Entry;

#[uniffi::export]
impl Persistence {
    #[uniffi::constructor]
    pub fn new() -> Arc<Self> {
        let runtime = tokio::runtime::Builder::new_current_thread()
            .enable_all()
            .build()
            .expect("Failed to create Tokio runtime");
        Arc::new(Self {
            db: Mutex::new(None),
            connection: Mutex::new(None),
            runtime,
        })
    }

    pub async fn connect(&self, database_path: String) {
        let mut db_guard = self.db.lock().unwrap();
        let mut conn_guard = self.connection.lock().unwrap();

        *conn_guard = None;
        *db_guard = None;

        let db = Database::open(&database_path).expect("Failed to open database");
        let conn = db.connect().expect("Failed to connect to database");

        *db_guard = Some(Arc::new(db));
        *conn_guard = Some(conn);
    }

    /// Execute a SQL query and return the results
    pub async fn execute(
        &self,
        sql: String,
        args: Vec<Entry>
    ) -> String {
        let conn_guard = self.connection.lock().unwrap();
        let conn = conn_guard.as_ref().expect("Not connected to database");
        let params = args.from_domain();
        self.runtime.block_on(async {
            if sql.trim().to_uppercase().starts_with("SELECT") {
                let mut rows = conn.query(&sql, params).await.unwrap();
                let mut results = Vec::new();
                while let Some(row) = rows.next().await.unwrap() {
                    let mut row_data = serde_json::Map::new();
                    for i in 0..row.column_count() {
                        let column_name = row.column_name(i).unwrap_or("").to_string();
                        row_data.insert(column_name, row.get_value(i).unwrap().to_json());
                    }
                    results.push(JsonValue::Object(row_data));
                }
                json!({ "changes": 0, "data": results }).to_string()
            } else {
                let affected_rows = conn.execute(&sql, params).await.unwrap();
                json!({ "changes": affected_rows, "data": [] }).to_string()
            }
        })
    }

    pub fn disconnect(&self) {
        let mut db_guard = self.db.lock().unwrap();
        let mut conn_guard = self.connection.lock().unwrap();

        *conn_guard = None;
        *db_guard = None;
    }
}
