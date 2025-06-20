<script setup>
import { inject, ref } from 'vue';
import { invoke } from '@tauri-apps/api/core';
import { useEvents } from '../composables/events.js';

const { events } = useEvents();

const renderDelTask = inject('renderDelTask');
const tasks = ref(events.tasks);
const task = ref(events.tasks[0].id);

function cancel() {
  renderDelTask.value = !renderDelTask.value;
}

function remove() {
  deleteTask(task.value);
}

async function deleteTask(uid) {
  try {
    const res = await invoke('delete_task', {
      userId: uid
    });
    console.log(res);
  } catch (error) {
    console.log(`Error: ${error}`);
  }
}
</script>

<template>
  <div class="event__page__box">
    <select v-model="task" name="task-choice">
      <option
        v-for="task in tasks"
        :value="task.id"
        :key="task.id">
        {{ task.name }}
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