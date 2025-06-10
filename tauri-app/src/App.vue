<script setup>
import { computed, ref } from 'vue';
import { RouterView, useRouter } from 'vue-router';
import LoginView from './views/LoginView.vue';

const router = useRouter();
const isLoggedIn = ref(false);
const darkMode = ref(false);
const darkModeClass = ref('dark');
const lightModeClass = ref('light');

function goToCalendar() {
  router.push('/');
}

function goToPlanner() {
  router.push('/plan');
}

function goToJournal() {
  router.push('/journal');
}

function loginToggle() {
  isLoggedIn.value = !isLoggedIn.value;
}

function darkModeToggle() {
  darkMode.value = !darkMode.value;
}

const darkModeIcon = computed(() => {
  return darkMode.value ? '🌚' : '🌞';
});
</script>

<template>
  <!-- 
    Keeping this for <img> with assets/ usage reference.
    <img alt="Vue logo" class="logo" src="@/assets/logo.svg" width="125" height="125" />
  -->
  <header>
    <img @click="goToCalendar" src="./assets/logo.png" alt="tpc placeholder logo" class="logo" width="80" height="80"/>
    <nav>
      <button @click="goToCalendar" class="router mode">Calendar</button>
      <button @click="goToPlanner" class="router mode">Planner</button>
      <button @click="goToJournal" class="router mode">Journal</button>
      <button @click="loginToggle" class="router login">{{ !isLoggedIn ? "Login" : "Logout" }}</button>
      <button @click="darkModeToggle" class="router" :class="[darkMode ? darkModeClass : lightModeClass]">{{ darkModeIcon }}</button>
    </nav>
  </header>
  <main class="wrapper">
    <RouterView v-if="isLoggedIn" />
    <LoginView v-else @login="loginToggle" />
  </main>

  <footer>
    <p><em>One Calendar App to rule them all, One Calendar App to find them; One Calendar App to bring them all and in the darkness bind them.</em></p>
    <p>04.2023 - 05.2025</p>
  </footer>
</template>

<style>
* {
  /* Core of the "light & dark theme" */
  background-color: #fff;
  color: #000;
}

header, footer {
  display: inline-flex;
  justify-content: flex-start;
  align-items: center;
}

.wrapper {
  /* border: 5px solid purple; */
  padding: 5px;
}

footer {
  height: 2vh;
  gap: 20px;
}

/* Buttons attached to Vue router and handling login and etc toggles*/
.router {
  margin: 10px;
  border: 1px solid gray;
  padding: 10px 20px;
  border: none;
  border-radius: 12px;
  font-size: 1.2rem;
}

.router.mode {
  background-color: indianred;
  color: #fff;
}

.router.login {
  background-color: cadetblue;
  color: #fff;
}

.router.dark {
  background-color: #2b2b2b;
  color: whitesmoke;
}

.router.light {
  background-color: lightgray;
  color: #2b2b2b;
}

.router.mode:hover {
  background-color: tomato;
}

.router.login:hover {
  background-color: teal;
}

.router.dark:hover {
  background-color: lightgray;
  color: #2b2b2b;
}

.router.light:hover {
  background-color: #2b2b2b;
  color: whitesmoke;
}

.router:hover {
  transition: all 0.3s ease;
  cursor: pointer;
}

.router:not(:hover) {
  transition: all 0.3s ease;
}

/* Some legacy general global styling */
img:hover {
  cursor: pointer;
}

.scrollbar {
  overflow-y: scroll;
  scrollbar-color: #000 #aaa;
  scrollbar-width: thin;
}

.scrollbar::-webkit-scrollbar {
  width: 5px;
  height: 8px;
  background-color: #aaa; 
}

.scrollbar::-webkit-scrollbar-thumb {
  background: #000;
}

.scrollbar-vertical {
  overflow-y: scroll;
  overflow-x: scroll;
  scrollbar-color: #000 #aaa;
  scrollbar-width: thin;
}

.scrollbar-vertical::-webkit-scrollbar {
  width: 8px;
  height: 8px;
  background-color: #aaa; 
}

.scrollbar-vertical::-webkit-scrollbar-thumb {
  background: #000;
}
</style>
