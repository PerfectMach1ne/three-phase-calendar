<script setup>
import { ref } from 'vue';
import { invoke } from '@tauri-apps/api/core';

const username = ref('Michalina Hatsuńska');
const email = ref('email@example.com');
const pwd = ref('');
const loginResult = ref('');
const noaccount = ref(null);
const registrationMode = ref(false);

async function attemptLogin() {
  try {
    if (registrationMode.value && noaccount.value) {
      const res = await invoke('login', {
        username: username.value,
        email: email.value,
        password: pwd.value
      });
    } else {
      const res = await invoke('login', {
        email: email.value,
        password: pwd.value
      });
    }
    
    loginResult.value = res;
    if (registrationMode.value) registrationMode.value = false;
  } catch (error) {
    loginResult.value = `Error: ${error}`;
    console.log(loginResult.value);
  }

  

  if (loginResult.value.includes('404')) {
    if (!registrationMode.value) loginResult.value = "Account with this email doesn't exist! Would you like to create one?";
    if (!noaccount.value || noaccount.value == null) initRegistration();
  } else if (loginResult.value.includes('401')) {
    // wrong password
  } else if (loginResult.value.includes('201')) {
    // account created
  } else if (loginResult.value.includes('200')) {
    // ordinary log-in
  }
}

function initRegistration() {
  if (noaccount.value == null) {
    noaccount.value = true;
  } else {
    noaccount.value = !noaccount.value;
    registrationMode.value = true;
    loginResult.value = "";
  } 
}
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
</style>
