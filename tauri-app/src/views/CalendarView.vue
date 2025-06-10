<script setup>
import NavbarContainer from "../components/containers/NavbarContainer.vue";
import ToolsContainer from "../components/containers/ToolsContainer.vue";
import CalendarContainer from "../components/containers/CalendarContainer.vue";
import AddTaskPopup from "../components/AddTaskPopup.vue";
import DeleteTaskPopup from "../components/DeleteTaskPopup.vue";
import AddTimeblockPopup from "../components/AddTimeblockPopup.vue";
import DeleteTimeblockPopup from "../components/containers/DeleteTimeblockPopup.vue";

import { ref, provide } from 'vue';

const renderAddTask = ref(false);
const renderAddTimeblock = ref(false);
const renderDelTask = ref(false);
const renderDelTimeblock = ref(false);
provide('renderAddTask', renderAddTask);
provide('renderAddTimeblock', renderAddTimeblock);
provide('renderDelTask', renderDelTask);
provide('renderDelTimeblock', renderDelTimeblock);
</script>

<template>
  <AddTaskPopup v-if="renderAddTask" />
  <DeleteTaskPopup v-if="renderDelTask"/>
  <AddTimeblockPopup v-if="renderAddTimeblock"/>
  <DeleteTimeblockPopup v-if="renderDelTimeblock"/>
  <main>
    <NavbarContainer class="nav__isolator" />
    <div class="calendar">
      <ToolsContainer />
      <CalendarContainer />
    </div>
  </main>
</template>

<style>
main {
  position: relative;
}

.nav__isolator {
  display: flex;
}

.calendar {
  display: flex;
}

div.event__page__buttons {
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
