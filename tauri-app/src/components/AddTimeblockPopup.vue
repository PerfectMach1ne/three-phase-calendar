<script setup>
import { inject, ref } from 'vue';

const renderAddTimeblock = inject('renderAddTimeblock');
const title = ref('(Untitled)');
const start_datetime = ref('2020-08-28');
const start_hours = ref('12:00');
const end_datetime = ref('2020-08-28');
const end_hours = ref('13:00');

function cancel() {
  renderAddTimeblock.value = !renderAddTimeblock.value;
}

function submit() {
  console.log(title.value + " "
   + start_datetime.value + "-" + end_datetime.value + " "
   + start_hours.value + "-" + end_hours.value);
  console.log("submit!");
}
</script>

<template>
  <div class="event__page__box">
    <span>
      <label for="timeblock-title-input">Title: </label>
      <input type="text" v-model="title" name="timeblock-title-input">
    </span>
    <span>
      <label for="tblock-start-datetime-input">Date &amp; time: </label>
      <input type="date" v-model="start_datetime" name="tblock-start-datetime-input">
    </span>
    <span>
      <label for="tblock-start-hours-choice">Hours: </label>
      <input list="hours-list" v-model="start_hours" name="tblock-start-hours-choice">
      <datalist id="hours-list">
        <option v-for="i in 24" :value="`${String(i - 1).padStart(2, '0') + ':00'}`"></option>
      </datalist>
    </span>
    <span>
      <label for="tblock-end-datetime-input">Date &amp; time: </label>
      <input type="date" v-model="end_datetime" name="tblock-end-datetime-input">
    </span>
    <span>
      <label for="tblock-end-hours-choice">Hours: </label>
      <input list="hours-list" v-model="end_hours" name="tblock-end-hours-choice">
      <datalist id="hours-list">
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
  min-height: 270px; 
  height: 270px;
  gap: 15px;
  margin: 5px;
  padding: 15px;
  border: 1px solid gray;
  font-family: Arial, Helvetica, sans-serif;
  font-size: 1.2rem;
}
</style>