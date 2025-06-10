<script setup>
import NavbarContainer from "../components/containers/NavbarContainer.vue";
import ToolsContainer from "../components/containers/ToolsContainer.vue";
import CalendarContainer from "../components/containers/CalendarContainer.vue";

import AddTaskView from "./button-views/AddTaskView.vue";
import { ref, provide } from 'vue';

const renderAddTask = ref(false);
provide('renderAddTask', renderAddTask);

function addTaskPopup() {
  console.log(renderAddTask.value);
  renderAddTask.value = true;
}
// const { toggleAddTask, renderAddTask } = inject('addTask');
</script>

<template>
    <AddTaskView v-if="renderAddTask" />
  <main>
    <NavbarContainer class="nav__isolator" />
    <div class="calendar">
      <ToolsContainer />
      <CalendarContainer />
    </div>
  </main>
</template>

<style scoped>
main {
  position: relative;
}

.nav__isolator {
  display: flex;
}

.calendar {
  display: flex;
}
</style>
