#[macro_export]
macro_rules! persistence_error {
    (connection) => { $crate::exception::persistence_exception::PersistenceException::CONNECTION_EXCEPTION };
    (open) => { $crate::exception::persistence_exception::PersistenceException::OPEN_EXCEPTION };
    (query) => { $crate::exception::persistence_exception::PersistenceException::QUERY_EXCEPTION };
    (credential) => { $crate::exception::persistence_exception::PersistenceException::CREDENTIAL_EXCEPTION };
    (sync) => { $crate::exception::persistence_exception::PersistenceException::SYNC_EXCEPTION };
    (remote) => { $crate::exception::persistence_exception::PersistenceException::REMOTE_CONNECTION_EXCEPTION };
    (init) => { $crate::exception::persistence_exception::PersistenceException::INITIALIZATION_EXCEPTION };
}
