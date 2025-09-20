pub struct PersistenceException;

impl PersistenceException {
    pub const INITIALIZATION_EXCEPTION: &'static str = "INITIALIZATION_EXCEPTION";
    pub const CREDENTIAL_EXCEPTION: &'static str = "CREDENTIAL_EXCEPTION";
    pub const CONNECTION_EXCEPTION: &'static str = "CONNECTION_EXCEPTION";
    pub const REMOTE_CONNECTION_EXCEPTION: &'static str = "REMOTE_CONNECTION_EXCEPTION";
    pub const OPEN_EXCEPTION: &'static str = "OPEN_EXCEPTION";
    pub const QUERY_EXCEPTION: &'static str = "QUERY_EXCEPTION";
    pub const SYNC_EXCEPTION: &'static str = "SYNC_EXCEPTION";
}
