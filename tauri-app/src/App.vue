<script setup>
import { RouterView, useRouter } from 'vue-router';

const router = useRouter();

function goToCalendar() {
  router.push('/');
}

function goToPlanner() {
  router.push('/plan');
}

function goToJournal() {
  router.push('/journal');
}
</script>

<template>
  <!-- 
    Keeping this for <img> with assets/ usage reference.
    <img alt="Vue logo" class="logo" src="@/assets/logo.svg" width="125" height="125" />
  -->
  <header>
    <img @click="goToHome" src="./assets/logo.png" alt="tpc placeholder logo" class="logo" width="80" height="80"/>
    <nav>
      <button @click="goToCalendar" class="router">Calendar</button>
      <button @click="goToPlanner" class="router">Planner</button>
      <button @click="goToJournal" class="router">Journal</button>
    </nav>
  </header>
  <main class="wrapper">
    <RouterView />
  </main>
  <footer>
    <p><em>One Calendar App to rule them all, One Calendar App to find them; One Calendar App to bring them all and in the darkness bind them.</em></p>
    <p>04.2023 - 03.2025</p>
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

/* Button attached to Vue router */
.router {
  margin: 10px;
  border: 1px solid gray;
  padding: 10px 20px;
  border: none;
  border-radius: 12px;
  background-color: indianred;
  font-size: 1.2rem;
  color: #fff;
}

.router:hover {
  background-color: tomato;
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
