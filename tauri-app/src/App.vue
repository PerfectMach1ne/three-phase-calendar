<script setup>
import { RouterLink, RouterView } from 'vue-router'
</script>

<script>
export default {
  methods: {
    goToHome() {
      this.$router.push('/');
    },
    goToTasks() {
      this.$router.push('/tasks');
    },
    goToAbout() {
      this.$router.push('/about');
    }
  }
}
</script>

<template>
  <!-- 
    Keeping this for <img> with assets/ usage reference.
    <img alt="Vue logo" class="logo" src="@/assets/logo.svg" width="125" height="125" />
  -->
  <header>
    <img @click="goToHome" src="./assets/logo.png" alt="tpc placeholder logo" class="logo" width="80" height="80"/>
    <nav> <!-- do NOT mistake this with "Navbar" as in component NavbarContainer and all related components -->
      <button @click="goToHome" class="router">Home</button>
      <button @click="goToTasks" class="router">Tasks</button>
      <button @click="goToAbout" class="router">About</button>
    </nav>
  </header>
  <div class="wrapper">
    <!-- <HelloWorld msg="You did it!" /> -->
    <RouterView />
  </div>
  <footer>
    <p>Helo this is the footer</p>
    <p>04.2023 - 02.2025</p>
  </footer>
</template>

<style>
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
