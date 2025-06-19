use sha2::{Sha256, Digest};
use reqwest;
use tauri::State;
use std::sync::Mutex;
use urlencoding::encode;
use tauri_plugin_store::StoreBuilder;
use jsonwebtoken::{decode, DecodingKey, Validation, Algorithm};

#[derive(serde::Serialize)]
struct AuthResponse {
    token: String,
    data: String,
}

#[derive(serde::Serialize)]
struct RegisterRequest {
    name: String,
    email: String,
    password: String,
}

#[derive(serde::Serialize)]
struct LoginRequest {
    email: String,
    password: String,
}

struct AppState {
    jwt_token: Mutex<Option<String>>,
}

// Listen. Don't ask. This is as "todo fix later" as it gets.
const PUBLIC_KEY: &str = r#"-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3SAbn4FoLgdFiuBdE83K
bfOzv+fxKpeEupod8GWR3+SyfOPKpLqr8SkcwDDl3mm0T9959hrvLtU6L9Dqpl/w
EnG89LmSLkibdB16g06YBOPiEQBz5zee80uLujhPGnrbsFMi25eLu8yh0YQVLpZu
UITb+Rz1WrKS7CbeDXYOwImRNrGpTb669Hzac4pCmtFbalDJqg4ITMOdJ1Hy9fnW
r2dpC+rrF0LvyyxPATJ4QF+B5ZrVlPngDDpt7j7cWsh3qossdMW3YmB2PNZ1KUSV
oh/ntmDS9id6Gy27yLC4JTjaoqVE466RD797y0Aj7iHIgAS9CFD9CtsPUwpcXBDI
9QIDAQAB
-----END PUBLIC KEY-----"#;

#[tauri::command]
async fn store_token_securely(
        token: String,
        app_handle: tauri::AppHandle,
        ) -> Result<(), String> {
    let store = StoreBuilder::new(&app_handle, ".auth.dat")
        .build()
        .map_err(|e| format!("Failed to create store: {}", e))?;
    store.set("jwt_token".to_string(), token);
    store.save().map_err(|e| format!("Failed to save store: {}", e))
}

#[tauri::command]
async fn load_token_securely(app_handle: tauri::AppHandle,) -> Result<Option<String>, String> {
    let store = StoreBuilder::new(&app_handle, ".auth.dat")
        .build()
        .map_err(|e| format!("Failed to create store: {}", e))?;

    println!("{}", store.get("jwt_token")
        .and_then(|v| v.as_str().map(|s| s.to_string())).unwrap());
    
    store.get("jwt_token")
        .and_then(|v| v.as_str().map(|s| s.to_string()))
        .map(Some)
        .ok_or("Failed to parse token".to_string())
}

#[tauri::command]
async fn clear_token_securely(app_handle: tauri::AppHandle,) -> Result<(), String> {
    let store = StoreBuilder::new(&app_handle, ".auth.dat")
        .build()
        .map_err(|e| format!("Failed to create store: {}", e))?;
    
    store.delete("jwt_token");
    
    store.save()
        .map_err(|e| format!("Failed to save store: {}", e))
}

#[tauri::command]
async fn fetch_cspace(
        userid: i32,
        state: tauri::State<'_, AppState>
        ) -> Result<String, String> {
    if userid <= 0 {
        return Err("Invalid userid (must be positive)".into());
    }
    let url = format!(
        "http://127.0.0.1:58057/api/login?id={}",
        encode(&userid.to_string())
    );

    let client = reqwest::Client::new();
    let token = state.jwt_token.lock()
        .map_err(|_| "Token mutex poisoned".to_string())?
        .as_ref()
        .ok_or("Unauthorized".to_string())?
        .clone();
    let response = client
        .get(&url)
        .header("Authorization", format!("Bearer {}",token))
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
    let body = RegisterRequest {
        name: username.to_string(),
        email: email.to_string(),
        password: hashed_password.to_string(),
    };
    let response = client
        .post("http://127.0.0.1:58057/api/register")
        .json(&body)
        .send()
        .await
        .map_err(|e| format!("Network error: {}", e))?;

    let status = response.status();
    let headers = response.headers().clone();
    let response_text = response.text()
        .await
        .map_err(|e| format!("Error reading response: {}", e))?;

    if !status.is_success() {
        return match status {
            reqwest::StatusCode::CONFLICT => Err("409 Conflict".to_string()),
            _ => Err(format!("Server error: {}", response_text))
        }
    }

    let token = headers.get("authorization")
                .and_then(|hv| hv.to_str().ok())
                .map(|s| s.trim_start_matches("Bearer ").to_string())
                .ok_or("Missing authorization header!")?;

    *state.jwt_token.lock().unwrap() = Some(token.clone());

    Ok(AuthResponse{ token, data: response_text })
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
    let body = LoginRequest {
        email: email.to_string(),
        password: hashed_password.to_string(),
    };
    let response = client
        .post("http://127.0.0.1:58057/api/login")
        .json(&body)
        // .json(&json!({
        //     "email": email,
        //     "password": hashed_password,
        // }))
        .send()
        .await
        .map_err(|e| format!("Network error: {}", e))?;

    let status = response.status();
    let headers = response.headers().clone();
    let response_text = response.text()
        .await
        .map_err(|e| format!("Error reading response: {}", e))?;
    
    if !status.is_success() {
        return match status {
            reqwest::StatusCode::NOT_FOUND => Err("404 Not Found".to_string()),
            _ => Err(format!("Server error: {}", response_text))

        }
    }

    let token = headers.get("authorization")
                .and_then(|hv| hv.to_str().ok())
                .map(|s| s.trim_start_matches("Bearer ").to_string())
                .ok_or("Missing authorization header!")?;
            
    
    *state.jwt_token.lock().unwrap() = Some(token.clone());

    Ok(AuthResponse{ token, data: response_text })
}

#[tauri::command]
async fn validate_token(token: String) -> Result<String, String> {
    let key = DecodingKey::from_rsa_pem(PUBLIC_KEY.as_bytes())
        .map_err(|e| format!("Bad key: {}", e))?;

    // Minimal validation for now - just checks signature and expiry.
    let validation = Validation::new(Algorithm::RS256);
    
    match decode::<serde_json::Value>(&token, &key, &validation) {
        Ok(_) => Ok(r#"{"valid":true}"#.to_string()),
        Err(e) => Err(format!(
            r#"{{"error":"ValidationError","message":"{}"}}"#, e
        ))
    }
}

#[cfg_attr(mobile, tauri::mobile_entry_point)]
pub fn run() {
    tauri::Builder::default()
        .plugin(tauri_plugin_store::Builder::default().build())
        .manage(AppState {
            jwt_token: Mutex::new(None),
        })
        .invoke_handler(tauri::generate_handler!
            [login, register, fetch_cspace,
             store_token_securely, load_token_securely, clear_token_securely,
             validate_token
        ])
        .run(tauri::generate_context!())
        .expect("error while running tauri application");
}
