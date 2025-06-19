<script setup>
import { ref } from 'vue';
import { invoke } from '@tauri-apps/api/core';
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css';

const greetMsg = ref("");
const name = ref("");

async function testGreet() {
  greetMsg.value = await invoke("greet", { name: name.value });
}
</script>

<script>
export default {

}
</script>

<template>
  <div class="journal__container scrollbar">
    <QuillEditor></QuillEditor>
    <form class="row" @submit.prevent="testGreet">
      <input id="greet-input" v-model="name" placeholder="Enter a name..." />
      <button type="submit">Greet</button>
    </form>
    <p>{{ greetMsg }}</p>
    <h1>
      Don't delete this just yet. Could be used for a nice little About page with
      a GitHub repo link and all the technologies listed nicely (just because it'd look cool).
    </h1>
    <p>AboutPage</p>
  </div>
</template>

<style>
.journal__container {
  margin: 5px;
  border: 2px solid gray;
  padding: 5px;
  max-height: 56vh;
  width: 85%
}
</style>