use sha2::{Sha256, Digest};
use reqwest;
use serde_json::json;

#[tauri::command]
fn greet(name: &str) -> String {
    format!("Hello, {}! You've been greeted from Rust!", name)
}

#[tauri::command]
async fn login(email: &str, password: &str) -> Result<String, String> {
    if email.is_empty() || password.is_empty() {
        return Err("Email and password field cannot be empty!".into());
    }

    let mut pwd_hasher = Sha256::new();
    pwd_hasher.update(password.as_bytes());
    let hashed_password = format!("{:x}", pwd_hasher.finalize());

    let client = reqwest::Client::new();
    let response = client
        .post("http://172.18.0.2:8057/api/login")
        .json(&json!({
            "email": email,
            "password": hashed_password,
        }))
        .send()
        .await
        .map_err(|e| format!("Network error: {}", e))?;

    let status = response.status();
    let response_text = response.text()
        .await
        .map_err(|e| format!("Error reading response: {}", e))?;

    if status.is_success() {
        Ok(response_text) // Return the actual response
    } else {
        Err(format!("Server error: {}", response_text))
    }
    // if response.status().is_success() {
    //     Ok(format!("Logged in as user ({}) successfully!", email))
    // } else {
    //     Err(format!("Login failed: {}", response.text().await.unwrap_or_default()))
    // }
}

#[cfg_attr(mobile, tauri::mobile_entry_point)]
pub fn run() {
    tauri::Builder::default()
        .plugin(tauri_plugin_opener::init())
        .invoke_handler(tauri::generate_handler![greet, login])
        .run(tauri::generate_context!())
        .expect("error while running tauri application");
}
