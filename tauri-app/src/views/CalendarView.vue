<script setup>
import NavbarContainer from '../components/containers/NavbarContainer.vue';
import ToolsContainer from '../components/containers/ToolsContainer.vue';
import CalendarContainer from '../components/containers/CalendarContainer.vue';
</script>

<template>
  <main>
    <NavbarContainer class="nav__isolator"/>
    <div class="calendar">
      <ToolsContainer/>
      <CalendarContainer/>
    </div>
  </main>
</template>

<style scoped>
.nav__isolator {
  display: flex;
}

.calendar {
  display: flex;
}
</style>
