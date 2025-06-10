<script setup>
import { inject } from 'vue';

const renderAddTask = inject('renderAddTask');

function cancel() {
  renderAddTask.value = !renderAddTask.value;
}
</script>

<template>
  <div class="event__page__box">
    <span>
      <label for="reminder-title-input">Title: </label>
      <input type="text" id="reminder-title-input" name="reminder-title-input">
    </span>
    <span>
      <label for="reminder-datetime-input">Date &amp; time: </label>
      <input type="datetime-local" id="reminder-datetime-input" name="reminder-datetime-input">
    </span>
    <!-- <span>
      <label for="hours-choice">Hours: </label>
      <input list="hours-list" id="hours-choice" name="hours-choice">
      <datalist id="hours-list">
        <option value=""></option>
        <option v-for="i in 24" v-bind:value="`${String(i - 1).padStart(2, '0') + ':00'}`"></option>
      </datalist>
    </span> -->
    <div class="event__page__buttons">
      <button>Create</button>
      <button @click="cancel">Cancel</button>
    </div>
  </div>
</template>

<style>
.event__page__box {
  z-index: 1;
  display: flex;
  position: fixed;
  top: 50%;
  left: 30%;
  right: 30%;
  flex-direction: column;
  align-content: center; 
  align-items: center;
  gap: 15px;
  margin: 5px;
  border: 1px solid gray;
  padding: 15px;
  width: 40%;
  font-family: Arial, Helvetica, sans-serif;
  font-size: 1.2rem;
}

.event__page__buttons {
  display: inline-flex;
  flex-direction: row;
  gap: 15px;
}

div.event__page__buttons > button {
  margin: 0;
  border: 1px solid gray;
  padding: 10px 20px;
  border: none;
  border-radius: 12px;
  background-color: aquamarine;
  font-size: 1rem;
  color: #000;
}

div.event__page__buttons > button:hover {
  background-color: turquoise;
  transition: all 0.3s ease;
  cursor: pointer;
}

div.event__page__buttons > button:not(:hover) {
  transition: all 0.3s ease;
}
</style>