use sha2::{Sha256, Digest};
use reqwest;
use serde_json::json;

#[tauri::command]
fn greet(name: &str) -> String {
    format!("Hello, {}! You've been greeted from Rust!", name)
}

#[tauri::command]
async fn register(username: &str, email: &str, password: &str) -> Result<String, String> {
    let username = if username.is_empty() { "Michalina Hatsuńska" } else { username };

    if email.is_empty() || password.is_empty() {
        return Err("Email and password field cannot be empty!".into());
    }

    let mut pwd_hasher = Sha256::new();
    pwd_hasher.update(password.as_bytes());
    let hashed_password = format!("{:x}", pwd_hasher.finalize());

    let client = reqwest::Client::new();

    let response = client
        .post("http://127.0.0.1:58057/api/register")
        .json(&json!({
            "name": username,
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
        Ok(response_text)
    } else {
        Err(format!("Server error: {}", response_text))
    }
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
        .post("http://127.0.0.1:58057/api/login")
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
        Ok(response_text)
    } else {
        Err(format!("Server error: {}", response_text))
    }
}

#[cfg_attr(mobile, tauri::mobile_entry_point)]
pub fn run() {
    tauri::Builder::default()
        .plugin(tauri_plugin_opener::init())
        .invoke_handler(tauri::generate_handler![greet, login, register])
        .run(tauri::generate_context!())
        .expect("error while running tauri application");
}
