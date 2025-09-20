#[derive(uniffi::Record, Debug, Clone)]
pub struct Credential {
    pub(crate) url: String,
    pub(crate) token: String,
    pub(crate) database: String,
}
