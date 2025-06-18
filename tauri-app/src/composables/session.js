/* 
 * src/composables/session.js
*/
import { ref } from 'vue';

const jwtToken = ref('');

export function useAuth() {
  const setToken = (token) => {
    jwtToken.value = token;
    // localStorage.setItem('jwt', token);
  };

  const loadToken = async () => { return jwtToken.value; };

  return {
    jwtToken,
    setToken,
    loadToken
  };
}