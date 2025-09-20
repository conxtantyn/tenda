use std::sync::{ Arc, Mutex };
use libsql::{ Database, Connection };
use tokio::runtime::Runtime;

use crate::model::credential::Credential;

#[derive(uniffi::Object)]
pub struct Persistence {
    pub(crate) credential: Mutex<Option<Credential>>,
    pub(crate) db: Mutex<Option<Arc<Database>>>,
    pub(crate) connection: Mutex<Option<Connection>>,
    pub(crate) runtime: Runtime,
}
