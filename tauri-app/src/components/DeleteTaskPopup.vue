<script setup>
import { inject, ref, onMounted } from 'vue';
import { invoke } from '@tauri-apps/api/core';
import { useEvents } from '../composables/events.js';

const { events, refreshTasks } = useEvents();

const renderDelTask = inject('renderDelTask');
const tasks = ref([]);
try {
  if (events.tasks !== null) tasks.value = events.tasks;
} catch (error) {
  console.error("You have no tasks!");
  tasks.value = [{
      hashcode: 1,
      name: "Create your first task!"
    }]
}

const task = ref(0);
try {
  if (events.tasks[0].hashcode !== null) task.value = events.tasks[0].hashcode;
} catch (error) {
  console.error("You have no tasks!");
  task.value = 1;
}

function cancel() {
  renderDelTask.value = !renderDelTask.value;
}

function remove() {
  deleteTask(task.value);
}

async function deleteTask(hashcode) {
  try {
    const res = await invoke('delete_task', {
      hashcode: hashcode
    });
    console.log(res);
  } catch (error) {
    console.log(`Error: ${error}`);
  }
}

onMounted(() => {
  refreshTasks();
})
</script>

<template>
  <div class="event__page__box">
    <select v-model="task" name="task-choice">
      <option
        v-for="task in tasks"
        :value="task.hashcode"
        :key="task.hashcode">
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