<script setup>
import { inject, ref } from 'vue';
import { invoke } from '@tauri-apps/api/core';
import { ViewType, Colors } from '../utils/enums.js';

const renderAddTimeblock = inject('renderAddTimeblock');
const title = ref('(Untitled)');
const description = ref("");
const start_datetime = ref('2020-08-28');
const start_hours = ref('12:00');
const end_datetime = ref('2020-08-28');
const end_hours = ref('13:00');
const viewtype = ref("static_task");
const color = ref('#c493d3');

function cancel() {
  renderAddTimeblock.value = !renderAddTimeblock.value;
}

function submit() {
  console.log(title.value + " "
   + start_datetime.value + "-" + end_datetime.value + " "
   + start_hours.value + "-" + end_hours.value);
  console.log("submit!");
}

async function createTimeblock() {
  try {
    const res = await invoke('create_timeblock', {
      userId: 14
    });
    console.log(res);
  } catch (error) {
    console.log(`Error: ${error}`);
  }
}
</script>

<template>
  <div class="event__page__box">
    <span>
      <label for="timeblock-title-input">Title: </label>
      <input type="text" v-model="title" name="timeblock-title-input">
    </span>
    <span>
      <label for="tblock-start-datetime-input">Start date: </label>
      <input type="date" v-model="start_datetime" name="tblock-start-datetime-input">
    </span>
    <span>
      <label for="tblock-start-hours-choice">Start time: </label>
      <input list="hours-list" v-model="start_hours" name="tblock-start-hours-choice">
      <datalist id="hours-list">
        <option v-for="i in 24" :value="`${String(i - 1).padStart(2, '0') + ':00'}`"></option>
      </datalist>
    </span>
    <span>
      <label for="tblock-end-datetime-input">End date: </label>
      <input type="date" v-model="end_datetime" name="tblock-end-datetime-input">
    </span>
    <span>
      <label for="tblock-end-hours-choice">End time: </label>
      <input list="hours-list" v-model="end_hours" name="tblock-end-hours-choice">
      <datalist id="hours-list">
        <option v-for="i in 24" :value="`${String(i - 1).padStart(2, '0') + ':00'}`"></option>
      </datalist>
    </span>
    <span>
      <label for="viewtype-choice">Type: </label>
      <select v-model="viewtype" name="viewtype-choice">
        <option
          v-for="taskview in Object.values(ViewType)"
          :value="taskview.viewtype"
          :key="taskview.viewtype">
          {{ taskview.friendlyname }}
          </option>
      </select>
    </span>
    <span>
      <label for="color-choice">Color: </label>
      <select list="color-list" v-model="color" name="color-choice">
        <option
          v-for="color in Object.values(Colors)"
          :value="color.hex"
          :key="color.hex">
          {{ color.name }}
        </option>
      </select>
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
  min-height: 370px; 
  height: 370px;
  gap: 15px;
  margin: 5px;
  padding: 15px;
  border: 1px solid gray;
  font-family: Arial, Helvetica, sans-serif;
  font-size: 1.2rem;
}
</style>