<script>
import NavbarContainer from '../components/containers/NavbarContainer.vue';
import ToolsContainer from '../components/containers/ToolsContainer.vue';
import CalendarContainer from '../components/containers/CalendarContainer.vue';

export default {
  components: {
    NavbarContainer,
    ToolsContainer,
    CalendarContainer
  }
}
</script>

<template>
  <main>
    <NavbarContainer class="nav__isolator"/>
    <div class="home">
      <ToolsContainer/>
      <CalendarContainer/>
    </div>
  </main>
</template>

<style scoped>
.nav__isolator {
  display: flex;
}
</style>
