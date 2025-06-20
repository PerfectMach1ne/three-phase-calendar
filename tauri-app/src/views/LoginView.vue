<script setup>
import { ref, onMounted } from 'vue';
import { invoke } from '@tauri-apps/api/core';
import { useAuth } from '../composables/session.js';

const { jwtToken, loadToken, setToken, userId, setUserId, clearToken } = useAuth();
const emit = defineEmits(['login']);

const username = ref('Michalina Hatsuńska');
const email = ref('email@example.com');
const pwd = ref('');
const loginResult = ref('');
const noaccount = ref(false);
const registrationMode = ref(false);
const isLoggedIn = ref(false);

async function attemptLogin() {
  try {
    if (registrationMode.value && !noaccount.value) {
      const res = await invoke('register', {
        username: username.value,
        email: email.value,
        password: pwd.value
      });
      let data = JSON.parse(res.data);
      setUserId(data.loginUserId);
      setToken(res.token);
      loginResult.value = data.result;
      console.log("register::" + JSON.stringify(data) + " " + res.token);
    } else {
      const res = await invoke('login', {
        email: email.value,
        password: pwd.value
      });
      let data = JSON.parse(res.data);
      if (data.result && !loginResult.value.includes('404')) {
        setUserId(data.loginUserId);
        setToken(res.token);
      }
      console.log("login::" + data.loginUserId);
      setUserId(data.loginUserId);
      loginResult.value = data.result;
      console.log("login::" + JSON.stringify(data) + " " + res.token);
    }
  } catch (e) {
    loginResult.value = `Error: ${e}`;
  }

  if (loginResult.value.includes('404') && !registrationMode.value) {
    noaccount.value = true;
    if (!registrationMode.value) {
      loginResult.value = "Account with this email doesn't exist! Would you like to create one?";
    }
  } else if (loginResult.value.includes('401') && !registrationMode.value) {
    loginResult.value = "Incorrect email or password!";
  } else if (loginResult.value.includes('409') && registrationMode.value) {
    loginResult.value = "Account with this email already exists!";
  } else if (loginResult.value.includes('201') && registrationMode.value) {
    loginResult.value = "Account has been created successfully!";
    registrationMode.value = false;
    isLoggedIn.value = true;

    setInterval(() => {
      emit('login');  
    }, 600);
  } else if (loginResult.value.includes('200')) {
    loginResult.value = "Successfully logged in!";
    isLoggedIn.value = true;

    setInterval(() => {
      emit('login');  
    }, 600);
  }
}

function initRegistration() {
  loginResult.value = "";
  noaccount.value = false;
  registrationMode.value = true;
}

onMounted(async () => {
  const token = await loadToken();
  if (token) {
    try {
      const res = await invoke('validate_token', { token });
      const data = JSON.parse(res);

      if (data.valid) {
        setToken(token);
        emit('login');
      }
    } catch (e) {
      console.error("Token validation failed:", e);
      await clearToken();
    }
  }
});
</script>

<template>
  <main>
    <div v-if="!registrationMode">
      <p>Email</p>
      <input type="email" v-model="email" />
      <p>Password</p>
      <input type="password" v-model="pwd" />
      <button @click="attemptLogin">Login</button>
    </div>
    <div v-else>
      <p>Name</p>
      <input type="text" v-model="username" />
      <p>Email</p>
      <input type="email" v-model="email" />
      <p>Password</p>
      <input type="password" v-model="pwd" />
      <button @click="attemptLogin">Login</button>
    </div>
    <p>{{ loginResult }}</p>
    <button v-if="noaccount" @click="initRegistration">Create an account</button>
  </main>
</template>
  
<style scoped>
.nav__isolator {
  display: flex;
}

.calendar {
  display: flex;
}

main {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin: 5px;
  border: 1px solid gray;
  padding: 5px;
  min-height: 400px;
  width: 98.5%;
}

main > div {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

button {
  margin: 10px;
  border: 1px solid gray;
  padding: 10px 20px;
  border: none;
  border-radius: 12px;
  background-color: aquamarine;
  font-size: 1rem;
  color: #000;
}

button:hover {
  background-color: turquoise;
  transition: all 0.3s ease;
  cursor: pointer;
}

button:not(:hover) {
  transition: all 0.3s ease;
}
</style>
