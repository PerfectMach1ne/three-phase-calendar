use sha2::{Sha256, Digest};
use reqwest;
use serde_json::json;
use tauri::State;
use std::sync::Mutex;

#[derive(serde::Serialize)]
struct AuthResponse {
    token: String,
    data: String,
}

struct AppState {
    jwt_token: Mutex<Option<String>>,
}

#[tauri::command]
fn greet(name: &str) -> String {
    format!("Hello, {}! You've been greeted from Rust!", name)
}

#[tauri::command]
async fn fetch_cspace(
        user_id: i32,
        state: tauri::State<'_, AppState>
        ) -> Result<String, String> {
    let url = format!("http://127.0.0.1:58057/api/login?id={user_id}");
    // let token = state.jwt_token.lock().unwrap()
    //     .as_ref() // or clone() ???
    //     .ok_or("Unauthorized".to_string())?;

    let client = reqwest::Client::new();
    let response = client
        .get(&url)
        .header(
            "Authorization",
            format!("Bearer {}",
                state.jwt_token.lock().unwrap()
                    .as_ref() // or clone() ???
                    .ok_or("Unauthorized".to_string())?)
        )
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
        Err(format!("Server error HTTP {}: {}", status, response_text))
    }
}

#[tauri::command]
async fn register(username: &str, email: &str, password: &str, state: State<'_, AppState>)
        -> Result<AuthResponse, String> {
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

    let token = response.headers()
        .get("authorization")
        .and_then(|hv| hv.to_str().ok())
        .map(|s| s.trim_start_matches("bearer ").to_string())
        .ok_or("Missing authorization header!")?;
    *state.jwt_token.lock().unwrap() = Some(token.clone().trim_start_matches("Bearer ").to_string());
    let status = response.status();
    let response_text = response.text()
        .await
        .map_err(|e| format!("Error reading response: {}", e))?;

    if status.is_success() {
        Ok(AuthResponse{
            token: token.clone(),
            data: response_text,
        })
    } else {
        Err(format!("Server error: {}", response_text))
    }
}

#[tauri::command]
async fn login(email: &str, password: &str, state: State<'_, AppState>)
        -> Result<AuthResponse, String> {
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

    let token = response.headers()
        .get("authorization")
        .and_then(|hv| hv.to_str().ok())
        .map(|s| s.trim_start_matches("bearer ").to_string())
        .ok_or("Missing authorization header!")?;
    *state.jwt_token.lock().unwrap() = Some(token.clone().trim_start_matches("Bearer ").to_string());
    let status = response.status();
    let response_text = response.text()
        .await
        .map_err(|e| format!("Error reading response: {}", e))?;
    
    if status.is_success() {
        Ok(AuthResponse{
            token: token.clone(),
            data: response_text,
        })
    } else {
        Err(format!("Server error: {}", response_text))
    }
}

#[cfg_attr(mobile, tauri::mobile_entry_point)]
pub fn run() {
    tauri::Builder::default()
        .plugin(tauri_plugin_opener::init())
        .manage(AppState {
            jwt_token: Mutex::new(None),
        })
        .invoke_handler(tauri::generate_handler![greet, login, register, fetch_cspace])
        .run(tauri::generate_context!())
        .expect("error while running tauri application");
}
