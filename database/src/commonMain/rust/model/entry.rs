#[derive(uniffi::Enum)]
pub enum Entry {
    Int(i64),
    Real(f64),
    Text(String),
    Null
}
