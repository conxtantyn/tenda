use uniffi::Enum;

#[derive(Enum)]
pub enum Entry {
    Int(i64),
    Real(f64),
    Text(String),
    Null
}
