use std::sync::{ Arc, Mutex };
use libsql::{ Database, Connection };
use tokio::runtime::Runtime;

#[derive(uniffi::Object)]
pub struct Persistence {
    pub(crate) db: Mutex<Option<Arc<Database>>>,
    pub(crate) connection: Mutex<Option<Connection>>,
    pub(crate) runtime: Runtime,
}
