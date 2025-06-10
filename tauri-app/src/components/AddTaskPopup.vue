<script setup>
import { inject, ref } from 'vue';

const renderAddTask = inject('renderAddTask');
const title = ref('(Untitled)');
const datetime = ref('2020-08-28');
const hours = ref('12:00');

function cancel() {
  renderAddTask.value = !renderAddTask.value;
}

function submit() {
  console.log(title.value + " " + datetime.value + " " + hours.value);
  console.log("submit!");
}
</script>

<template>
  <div class="event__page__box">
    <span>
      <label for="reminder-title-input">Title: </label>
      <input type="text" v-model="title" name="reminder-title-input">
    </span>
    <span>
      <label for="reminder-datetime-input">Date &amp; time: </label>
      <input type="date" v-model="datetime" name="reminder-datetime-input">
    </span>
    <span>
      <label for="hours-choice">Hours: </label>
      <input list="hours-list" v-model="hours" name="hours-choice">
      <datalist id="hours-list" >
        <option v-for="i in 24" :value="`${String(i - 1).padStart(2, '0') + ':00'}`"></option>
      </datalist>
    </span>
    <div class="event__page__buttons">
      <button @click="submit">Create</button>
      <button @click="cancel">Cancel</button>
    </div>
    <p>a</p>
  </div>
</template>

<style>
.event__page__box {
  z-index: 1;
  display: flex;
  position: fixed;
  top: 30%;
  bottom: 0;
  left: 30%;
  right: 30%;
  max-width: 35%;
  min-height: 200px; 
  height: 200px;
  gap: 15px;
  margin: 5px;
  border: 1px solid gray;
  font-family: Arial, Helvetica, sans-serif;
  font-size: 1.2rem;
}

.event__page__buttons {
  /* display: inline-flex;
  flex-direction: row;
  gap: 15px; */
}

div.event__page__buttons > button {
  /* margin: 0;
  border: 1px solid gray;
  padding: 10px 20px;
  border: none;
  border-radius: 12px;
  background-color: aquamarine;
  font-size: 1rem;
  color: #000; */
}

div.event__page__buttons > button:hover {
  /* background-color: turquoise;
  transition: all 0.3s ease;
  cursor: pointer; */
}

div.event__page__buttons > button:not(:hover) {
  /* transition: all 0.3s ease; */
}
</style>