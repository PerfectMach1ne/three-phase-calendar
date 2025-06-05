<script setup>
import { ref } from 'vue';
import { invoke } from '@tauri-apps/api/core';

const username = ref('Michalina Hatsuńska');
const email = ref('email@example.com');
const pwd = ref('');
const loginResult = ref('');
const noaccount = ref(false);
const registrationMode = ref(false);

async function attemptLogin() {
  console.log(registrationMode.value + " " + noaccount.value);
  try {
    if (registrationMode.value && !noaccount.value) {
      const res = await invoke('register', {
        username: username.value,
        email: email.value,
        password: pwd.value
      });
      loginResult.value = res;
    } else {
      const res = await invoke('login', {
        email: email.value,
        password: pwd.value
      });
      loginResult.value = res;
    }
    
    
  } catch (error) {
    loginResult.value = `Error: ${error}`;
    console.log(loginResult.value);
  }

  if (loginResult.value.includes('404') && !registrationMode.value) {
    noaccount.value = true;
    if (!registrationMode.value) loginResult.value = "Account with this email doesn't exist! Would you like to create one?";
  } else if (loginResult.value.includes('401') && !registrationMode.value) {
    loginResult.value = "Incorrect email or password!";
  } else if (loginResult.value.includes('201') && registrationMode.value) {
    loginResult.value = "Account has been created successfully!";
  } else if (loginResult.value.includes('200')) {
    loginResult.value = "Successfully logged in!";
  }
}

function initRegistration() {
  loginResult.value = "";
  noaccount.value = false;
  registrationMode.value = true;
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
