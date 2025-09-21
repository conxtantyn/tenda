use serde_json::json;

trait Exception<T> {
    fn unwrap_or_panic(self, code: String) -> T;
}

impl<T, E: ToString> Exception<T> for Result<T, E> {
    fn unwrap_or_panic(self, code: String) -> T {
        self.unwrap_or_else(|e| {
            panic!(
                "{}",
                json!({
                    "status": code,
                    "reason": e.to_string(),
                })
                .to_string()
            )
        })
    }
}
