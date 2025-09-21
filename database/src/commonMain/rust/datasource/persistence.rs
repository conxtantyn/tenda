use std::sync::{ Arc, RwLock };
use libsql::{ Database, Connection };
use tokio::runtime::Runtime;

use crate::model::credential::Credential;

#[derive(uniffi::Object)]
pub struct Persistence {
    pub(crate) credential: RwLock<Option<Credential>>,
    pub(crate) db: RwLock<Option<Arc<Database>>>,
    pub(crate) connection: RwLock<Option<Connection>>,
    pub(crate) runtime: Runtime,
}
