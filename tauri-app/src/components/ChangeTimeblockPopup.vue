<script setup>
import { inject, ref, onMounted } from 'vue';
import { invoke } from '@tauri-apps/api/core';
import { ViewType, Colors } from '../utils/enums.js';
import { useEvents } from '../composables/events.js';

const { events, refreshTimeblocks } = useEvents();

const renderChangeTimeblock = inject('renderChangeTimeblock');
const timeblocks = ref([]);
try {
  if (events.timeblocks !== null) timeblocks.value = events.timeblocks;
} catch (error) {
  console.error("You have no timeblocks!");
  timeblocks.value = [{
      hashcode: 1,
      name: "Create your first timeblock!"
    }]
}

const timeblock = ref(0);
try {
  if (events.timeblocks[0].hashcode !== null) timeblock.value = events.timeblocks[0].hashcode;
} catch (error) {
  console.error("You have no timeblocks!");
  timeblock.value = 1;
}

const title = ref('(Untitled)');
const description = ref("")
const start_date = ref('2020-08-28');
const start_time = ref('12:00');
const end_date = ref('2020-08-28');
const end_time = ref('13:00');
const viewtype = ref("static_task");
const color = ref('#c493d3');

function cancel() {
  renderChangeTimeblock.value = !renderChangeTimeblock.value;
}

function change() {
  changeTimeblock(timeblock.value);
}

async function changeTimeblock(hashcode) {
  try {
    console.log(hashcode);
    const res = await invoke('update_timeblock', {
      hashcode: hashcode,
      startDatetime: start_date.value + "T" + start_time.value,
      endDatetime: end_date.value + "T" + end_time.value, 
      name: title.value,
      desc: description.value,
      viewtype: viewtype.value,
      color: {
        hasColor: true,
        hex: color.value
      }
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
    <span>
      <label for="timeblock-title-input">Title: </label>
      <input type="text" v-model="title" name="timeblock-title-input">
    </span>
    <span>
      <label for="tblock-start-date-input">Start date: </label>
      <input type="date" v-model="start_date" name="tblock-start-date-input">
    </span>
    <span>
      <label for="tblock-start--choice">Start time: </label>
      <input list="time-list" v-model="start_time" name="tblock-start-time-choice">
      <datalist id="time-list">
        <option v-for="i in 24" :value="`${String(i - 1).padStart(2, '0') + ':00'}`"></option>
      </datalist>
    </span>
    <span>
      <label for="tblock-end-date-input">End date: </label>
      <input type="date" v-model="end_date" name="tblock-end-date-input">
    </span>
    <span>
      <label for="tblock-end-time-choice">End time: </label>
      <input list="time-list" v-model="end_time" name="tblock-end-time-choice">
      <datalist id="time-list">
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
      <button @click="change">Change</button>
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
  max-width: 45%;
  min-height: 355px; 
  max-height: 600px;
  height: 420px;
  
  gap: 15px;
  margin: 5px;
  padding: 15px;
  border: 1px solid gray;

  font-family: Arial, Helvetica, sans-serif;
  font-size: 1.2rem;
}
</style>