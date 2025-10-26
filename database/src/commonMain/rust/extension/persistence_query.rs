use serde_json::{ json, Value as JsonValue };

use crate::persistence_error;
use crate::datasource::persistence::Persistence;
use crate::exception::throwable::Throwable;
use crate::mapper::entry_mapper::EntryMapper;
use crate::mapper::json_mapper::JsonMapper;
use crate::model::entry::Entry;

pub(crate) trait PersistenceQuery {
    async fn get(&self, sql: String, args: Vec<Entry>) -> String;
    async fn post(&self, sql: String, args: Vec<Entry>) -> String;
}

impl PersistenceQuery for Persistence {
    /// Execute a SELECT query and return the results
    async fn get(
        &self,
        sql: String,
        args: Vec<Entry>
    ) -> String {
        let conn_guard = self.connection.read().unwrap();
        let conn = conn_guard.as_ref().unwrap_or_panic(persistence_error!(connection));
        let params = args.from_domain();
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
    }

    /// Execute a non-SELECT SQL statement (INSERT, UPDATE, DELETE, etc.)
    /// and return affected row count
    async fn post(
        &self,
        sql: String,
        args: Vec<Entry>
    ) -> String {
        let conn_guard = self.connection.read().unwrap();
        let conn = conn_guard.as_ref().unwrap_or_panic(persistence_error!(connection));
        let params = args.from_domain();
        let affected_rows = conn.execute(&sql, params).await.unwrap();
        json!({ "changes": affected_rows, "data": [] }).to_string()
    }
}
