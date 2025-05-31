// Learn more about Tauri commands at https://tauri.app/develop/calling-rust/
#[tauri::command]
fn greet(name: &str) -> String {
    format!("Hello, {}! You've been greeted from Rust!", name)
}

#[tauri::command]
async fn login(email: &str, password: &str) -> Result<String, String> {
    if email.is_empty() || password.is_empty() {
        return Err("Email and password field cannot be empty!".into());
    }

    Ok(format!("Logged in as user ({}) successfully!", email))
}

#[cfg_attr(mobile, tauri::mobile_entry_point)]
pub fn run() {
    tauri::Builder::default()
        .plugin(tauri_plugin_opener::init())
        .invoke_handler(tauri::generate_handler![greet, login])
        .run(tauri::generate_context!())
        .expect("error while running tauri application");
}
