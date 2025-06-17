<script setup>
import NavbarContainer from '../components/containers/NavbarContainer.vue';
import ToolsContainer from '../components/containers/ToolsContainer.vue';
import JournalContainer from '../components/containers/JournalContainer.vue';
</script>

<template>
  <main>
    <NavbarContainer class="nav__isolator"/>
    <div class="journal">
      <ToolsContainer/>
      <JournalContainer/>
    </div>
  </main>
</template>

<style scoped>
.nav__isolator {
  display: flex;
}

.journal {
  display: flex;
}
</style>
