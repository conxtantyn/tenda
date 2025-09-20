use libsql::Value;
use serde_json::{Value as JsonValue, json};

pub(crate) trait JsonMapper {
    fn to_json(&self) -> JsonValue;
}

impl JsonMapper for Value {
    fn to_json(&self) -> JsonValue {
        match self {
            Value::Null => JsonValue::Null,
            Value::Integer(i) => json!(i),
            Value::Real(f) => json!(f),
            Value::Text(s) => json!(s),
            Value::Blob(b) => json!(b),
        }
    }
}
