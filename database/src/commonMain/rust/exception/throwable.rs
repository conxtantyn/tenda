use serde_json::json;

pub trait Throwable<T> {
    fn expect_or_panic<S: Into<String>>(self, code: S) -> T;

    fn unwrap_or_panic<S: Into<String>>(self, code: S) -> T;
}

impl<T, E: std::fmt::Debug> Throwable<T> for Result<T, E> {
    fn expect_or_panic<S: Into<String>>(self, code: S) -> T {
        self.unwrap_or_else(|e| {
            panic!(
                "{}",
                json!({
                    "status": code.into(),
                    "reason": format!("{:?}", e),
                })
                .to_string()
            )
        })
    }

    fn unwrap_or_panic<S: Into<String>>(self, code: S) -> T {
        self.unwrap_or_else(|e| {
            panic!(
                "{}",
                json!({
                    "status": code.into(),
                    "reason": format!("{:?}", e),
                })
                .to_string()
            )
        })
    }
}

impl<T> Throwable<T> for Option<T> {
    fn expect_or_panic<S: Into<String>>(self, code: S) -> T {
        self.unwrap_or_else(|| {
            panic!(
                "{}",
                json!({ "status": code.into() }).to_string()
            )
        })
    }

    fn unwrap_or_panic<S: Into<String>>(self, code: S) -> T {
        self.unwrap_or_else(|| {
            panic!(
                "{}",
                json!({ "status": code.into() }).to_string()
            )
        })
    }
}
