use libsql::{ Database, Builder };
use std::sync::{ Arc, RwLock };
use hyper_rustls::HttpsConnectorBuilder;

use crate::persistence_error;
use crate::datasource::persistence::Persistence;
use crate::exception::throwable::Throwable;
use crate::extension::persistence_query::PersistenceQuery;
use crate::model::entry::Entry;
use crate::model::credential::Credential;

#[uniffi::export]
impl Persistence {
    #[uniffi::constructor]
    pub fn new() -> Arc<Self> {
        let runtime = tokio::runtime::Builder::new_current_thread()
            .enable_all()
            .build()
            .unwrap_or_panic(persistence_error!(init));
        Arc::new(Self {
            credential: RwLock::new(None),
            db: RwLock::new(None),
            connection: RwLock::new(None),
            runtime,
        })
    }

    pub fn connect(&self, credential: Credential) {
        let mut cred_guard = self.credential.write().unwrap();
        let mut db_guard = self.db.write().unwrap();
        let mut conn_guard = self.connection.write().unwrap();

        *conn_guard = None;
        *db_guard = None;

        let db = Database::open(&credential.database).unwrap_or_panic(persistence_error!(open));
        let conn = db.connect().unwrap_or_panic(persistence_error!(connection));

        *cred_guard = Some(credential);
        *db_guard = Some(Arc::new(db));
        *conn_guard = Some(conn);
    }

    /// Execute a SQL query and return the results
    /// (wrapper function for backward compatibility)
    pub async fn execute(
        &self,
        sql: String,
        args: Vec<Entry>
    ) -> String {
        if sql.trim().to_uppercase().starts_with("SELECT") {
            self.get(sql, args).await
        } else {
            self.post(sql, args).await
        }
    }

    pub async fn synchronise(&self) {
        let conn_guard = self.credential.read().unwrap();
        let credential = conn_guard.as_ref().expect_or_panic(persistence_error!(credential));
        let https = HttpsConnectorBuilder::new()
            .with_webpki_roots()
            .https_or_http()
            .enable_http1()
            .build();
        let client = Builder::new_remote_replica(
            credential.database.clone(),
            credential.url.clone(),
            credential.token.clone()
        ).connector(https)
        .build();
        self.runtime.block_on(async {
            client.await
                .unwrap_or_panic(persistence_error!(sync))
                .sync()
                .await
                .unwrap_or_panic(persistence_error!(sync));
        });
    }

    pub fn disconnect(&self) {
        let mut cred_guard = self.credential.write().unwrap();
        let mut db_guard = self.db.write().unwrap();
        let mut conn_guard = self.connection.write().unwrap();

        *cred_guard = None;
        *conn_guard = None;
        *db_guard = None;
    }
}
