use libsql::{ Builder };
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
        let runtime = tokio::runtime::Runtime::new()
            .unwrap_or_panic(persistence_error!(init));
        Arc::new(Self {
            credential: RwLock::new(None),
            db: RwLock::new(None),
            connection: RwLock::new(None),
            runtime,
        })
    }

    pub fn connect(&self, credential: Credential) {
        {
            let mut cred_guard = self.credential.write().unwrap();
            let mut db_guard = self.db.write().unwrap();
            let mut conn_guard = self.connection.write().unwrap();
            *conn_guard = None;
            *db_guard = None;
            *cred_guard = None;
        }
        self.handleSynchronise(credential.clone());
        let https = HttpsConnectorBuilder::new()
            .with_webpki_roots()
            .https_or_http()
            .enable_http1()
            .build();
        let (arc_db, conn) = self.runtime.block_on(async {
            let db = Builder::new_synced_database(
                credential.database.clone(),
                credential.url.clone(),
                credential.token.clone()
            ).connector(https)
                .build()
                .await
                .unwrap_or_panic(persistence_error!(open));
            let arc_db = Arc::new(db);
            let conn = arc_db.connect().unwrap_or_panic(persistence_error!(connection));
            (arc_db, conn)
        });
        {
            let mut cred_guard = self.credential.write().unwrap();
            let mut db_guard = self.db.write().unwrap();
            let mut conn_guard = self.connection.write().unwrap();
            *db_guard = Some(arc_db);
            *conn_guard = Some(conn);
            *cred_guard = Some(credential);
        }
    }

    /// Execute a SQL query and return the results
    /// (wrapper function for backward compatibility)
    pub fn execute(
        &self,
        sql: String,
        args: Vec<Entry>
    ) -> String {
        self.runtime.block_on(async {
            if sql.trim().to_uppercase().starts_with("SELECT") {
                 self.get(sql, args).await
            } else {
                 self.post(sql, args).await
            }
        })
    }

    pub fn synchronise(&self) {
        let cred_guard = self.credential.read().unwrap();
        let credential = cred_guard.as_ref().expect_or_panic(persistence_error!(credential));
        self.handleSynchronise(credential.clone());
    }

    fn handleSynchronise(&self, credential: Credential) {
        let https = HttpsConnectorBuilder::new()
            .with_webpki_roots()
            .https_or_http()
            .enable_http1()
            .build();
        let client = Builder::new_synced_database(
            credential.database.clone(),
            credential.url.clone(),
            credential.token.clone()
        ).connector(https).build();
        self.runtime.block_on(async {
            let db = client.await.unwrap_or_panic(persistence_error!(sync));
            db.sync()
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
