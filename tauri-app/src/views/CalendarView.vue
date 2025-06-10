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
