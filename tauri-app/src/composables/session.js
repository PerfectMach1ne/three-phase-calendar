/* 
 * src/composables/session.js
*/
import { ref } from 'vue';
import { invoke } from '@tauri-apps/api/core';

const jwtToken = ref('');
const userId = ref(0);

export function useAuth() {
  const setToken = async (token) => {
    jwtToken.value = token;
    console.log(jwtToken.value || "[INFO::session.js:13] jwtToken is uninitialized.");
    userId.value = await invoke('extract_user_id', { token });
    console.log(userId.value || "[INFO::session.js:15] userId is uninitialized.");
    await invoke('store_token_securely', { token });
  };

  const setUserId = (uid) => {
    userId.value = uid;
  };

  const loadToken = async () => { 
    try {
      const token = await invoke('load_token_securely');
      if (token) {
        jwtToken.value = token;
        userId.value = await invoke('extract_user_id', { token });
      } 
      return token;
    } catch (e) {
      console.error("Token load failed:", e);
      return null;
    }
  };

  const loadUserId = async () => { return userId.value };

  const clearToken = async () => {
    jwtToken.value = '';
    await invoke('clear_token_securely');
  };

  const clearUserId = async () => {
    userId.value = '';
  }

  return {
    jwtToken,
    setToken,
    loadToken,
    userId,
    setUserId,
    loadUserId,
    clearToken,
    clearUserId
  };
}