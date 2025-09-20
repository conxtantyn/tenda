use std::sync::{Arc, Mutex};
use libsql::{Database, Connection, Value as LibSqlValue, Error as LibSqlError};
use uniffi::Enum;
use uniffi::Record;
use tokio::runtime::Runtime;
use serde_json::{json, Value as JsonValue};

#[derive(Record)]
pub struct Type {
    pub text: Option<String>,
    pub integer: Option<i64>,
    pub real: Option<f64>,
    pub blob: Option<Vec<u8>>,
    pub is_null: bool,
}

#[derive(Record)]
pub struct Response {
    pub columns: Vec<Type>,
}

#[derive(Enum)]
pub enum Argument {
    Int(i64),
    Real(f64),
    Text(String),
    Null,
}

#[derive(uniffi::Object)]
pub struct Persistence {
    db: Mutex<Option<Arc<Database>>>,
    connection: Mutex<Option<Connection>>,
    runtime: Runtime,
}

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
        args: Vec<Argument>
    ) -> String {
        let conn_guard = self.connection.lock().unwrap();
        let conn = conn_guard.as_ref().expect("Not connected to database");
        let mut params = Vec::new();
        for arg in args {
            let val = match arg {
                Argument::Int(i) => LibSqlValue::Integer(i),
                Argument::Real(f) => LibSqlValue::Real(f),
                Argument::Text(s) => LibSqlValue::Text(s),
                Argument::Null => LibSqlValue::Null,
            };
            params.push(val);
        }
        self.runtime.block_on(async {
            if sql.trim().to_uppercase().starts_with("SELECT") {
                let mut rows = conn.query(&sql, params).await.unwrap();
                let mut results = Vec::new();
                while let Some(row) = rows.next().await.unwrap() {
                    let mut row_data = serde_json::Map::new();
                    for i in 0..row.column_count() {
                        let column_name = row.column_name(i).unwrap_or("").to_string();
                        let value = match row.get_value(i).unwrap() {
                            LibSqlValue::Null => JsonValue::Null,
                            LibSqlValue::Integer(i) => json!(i),
                            LibSqlValue::Real(f) => json!(f),
                            LibSqlValue::Text(s) => json!(s),
                            LibSqlValue::Blob(b) => json!(b),
                        };
                        row_data.insert(column_name, value);
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

uniffi::setup_scaffolding!();
