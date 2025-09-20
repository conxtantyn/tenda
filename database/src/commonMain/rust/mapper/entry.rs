use libsql::Value;
use crate::model::entry::Entry;

pub(crate) trait EntryMapper {
    fn from_domain(&self) -> Vec<Value>;
}

impl EntryMapper for Vec<Entry> {
    fn from_domain(&self) -> Vec<Value> {
        let mut params = Vec::new();
        for arg in self {
            let val = match arg {
                Entry::Int(i) => Value::Integer(*i),
                Entry::Real(f) => Value::Real(*f),
                Entry::Text(s) => Value::Text(s.clone()),
                Entry::Null => Value::Null,
            };
            params.push(val);
        }
        params
    }
}
