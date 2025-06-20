<script setup>
import { inject, ref, onMounted } from 'vue';
import { invoke } from '@tauri-apps/api/core';
import { useEvents } from '../composables/events.js';

const { events, refreshTimeblocks } = useEvents();

const renderDelTimeblock = inject('renderDelTimeblock');
const timeblocks = ref([]);
try {
  if (events.timeblocks !== null) timeblocks.value = events.timeblocks;
  else timeblocks.value = [{
      hashcode: 1,
      name: "Create your first timeblock!"
    }]
} catch (error) { console.error("You have no timeblocks!"); }

const timeblock = ref(0);
try {
  if (events.timeblocks[0].hashcode !== null) timeblock.value = events.timeblocks[0].hashcode;
  else timeblock = 1;
} catch (error) { console.error("You have no timeblocks!"); }

function cancel() {
  renderDelTimeblock.value = !renderDelTimeblock.value;
}

function remove() {
  deleteTimeblock(timeblock.value);
}

async function deleteTimeblock(hashcode) {
  try {
    const res = await invoke('delete_timeblock', {
      hashcode: hashcode
    });
    console.log(res);
  } catch (error) {
    console.log(`Error: ${error}`);
  }
}

onMounted(() => {
  refreshTimeblocks();
})
</script>

<template>
  <div class="event__page__box">
    <select v-model="timeblock" name="timeblock-choice">
      <option
        v-for="timeblock in timeblocks"
        :value="timeblock.hashcode"
        :key="timeblock.hashcode">
        {{ timeblock.name }}
        </option>
    </select>
    <div class="event__page__buttons">
      <button @click="remove">Remove</button>
      <button @click="cancel">Cancel</button>
    </div>
  </div>
</template>

<style scoped>
.event__page__box {
  z-index: 1;

  overflow: hidden;
  resize: both;

  display: flex;
  flex-direction: column;
  align-items: center;
  position: fixed;

  top: 30%;
  left: 30%;
  right: 30%;

  min-width: 200px;
  max-width: 35%;
  min-height: 100px; 
  max-height: 100px; 
  height: 100px;

  gap: 15px;
  margin: 5px;
  padding: 15px;
  border: 1px solid gray;

  font-family: Arial, Helvetica, sans-serif;
  font-size: 1.2rem;
}
</style>