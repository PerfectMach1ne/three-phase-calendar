<script setup>
import { inject, ref } from 'vue';

const renderDelTask = inject('renderDelTask');
const task = ref("");
const tasks = ref([]);

function cancel() {
  renderDelTask.value = !renderDelTask.value;
}
</script>

<template>
  <div class="event__page__box">
    <select v-model="task" name="task-choice">
      <option
        v-for="task in tasks"
        :value="task.hash"
        :key="task.hash">
        {{ task.name }}
        </option>
    </select>
    <div class="event__page__buttons">
      <button>Remove</button>
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