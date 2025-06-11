<script setup>
import { inject, ref } from 'vue';

const renderAddTask = inject('renderAddTask');
const title = ref('(Untitled)');
const date = ref('2020-08-28');
const time = ref('12:00');

function cancel() {
  renderAddTask.value = !renderAddTask.value;
}

function submit() {
  const datetime = new Date(date.value + "T" + time.value);
  console.log(datetime);
  console.log("submit!");
}
</script>

<template>
  <div class="event__page__box">
    <span>
      <label for="task-title-input">Title: </label>
      <input type="text" v-model="title" name="task-title-input">
    </span>
    <span>
      <label for="task-date-input">Date &amp; time: </label>
      <input type="date" v-model="date" name="task-date-input">
    </span>
    <span>
      <label for="task-time-choice">Hours: </label>
      <input list="time-list" v-model="time" name="task-time-choice">
      <datalist id="time-list">
        <option v-for="i in 24" :value="`${String(i - 1).padStart(2, '0') + ':00'}`"></option>
      </datalist>
    </span>
    <div class="event__page__buttons">
      <button @click="submit">Create</button>
      <button @click="cancel">Cancel</button>
    </div>
  </div>
</template>

<style scoped>
.event__page__box {
  z-index: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  position: fixed;
  top: 30%;
  left: 30%;
  right: 30%;
  max-width: 35%;
  min-height: 180px; 
  height: 180px;
  gap: 15px;
  margin: 5px;
  padding: 15px;
  border: 1px solid gray;
  font-family: Arial, Helvetica, sans-serif;
  font-size: 1.2rem;
}
</style>