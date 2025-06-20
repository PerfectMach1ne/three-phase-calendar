<script setup>
import { inject, ref, onMounted } from 'vue';
import { invoke } from '@tauri-apps/api/core';
import { ViewType, Colors } from '../utils/enums.js';
import { useEvents } from '../composables/events.js';

const { events, refreshTasks } = useEvents();

const renderChangeTask = inject('renderChangeTask');

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
  task = 1;
}

const renderAddTask = inject('renderAddTask');
const title = ref('(Untitled)');
const description = ref("");
const date = ref('2020-08-28');
const time = ref('12:00');
const viewtype = ref("static_task");
const color = ref('#c493d3');
const isdone = ref(false);

function cancel() {
  renderChangeTask.value = !renderChangeTask.value;
}

function change() {
  changeTask(task.value);
}

async function changeTask(hashcode) {
  try {
    const res = await invoke('update_task', {
      hashcode: hashcode,
      datetime: date.value + "T" + time.value,
      name: title.value,
      desc: description.value,
      viewtype: viewtype.value,
      color: {
        hasColor: true,
        hex: color.value
      },
      isdone: isdone.value
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
  max-width: 35%;
  min-height: 300; 
  max-height: 500px; 
  height: 335px;

  gap: 15px;
  margin: 5px;
  padding: 15px;
  border: 1px solid gray;

  font-family: Arial, Helvetica, sans-serif;
  font-size: 1.2rem;
}
</style>