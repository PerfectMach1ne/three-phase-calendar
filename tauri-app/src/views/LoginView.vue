<script setup>
import { ref } from 'vue';
import { invoke } from '@tauri-apps/api/core';

const email = ref('email@example.com');
const pwd = ref('');
const loginResult = ref('');

async function attemptLogin() {
  try {
    loginResult.value = await invoke('login', {
      email: email.value,
      password: pwd.value
    });
    console.log(loginResult.value);
  } catch (error) {
    loginResult.value = `Error: ${error}`;
  }
}
</script>

<template>
  <main>
    <p>Email</p>
    <input type="email" v-model="email" />
    <p>Password</p>
    <input type="password" v-model="pwd" />
    <button @click="attemptLogin">Login</button>
    <p>{{ loginResult }}</p>
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
</style>
