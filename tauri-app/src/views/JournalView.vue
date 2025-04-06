<script>
import NavbarContainer from '../components/containers/NavbarContainer.vue';
import ToolsContainer from '../components/containers/ToolsContainer.vue';
import AboutContainer from '../components/containers/AboutContainer.vue';

export default {
  components: {
    NavbarContainer,
    ToolsContainer,
    AboutContainer
  }
}
</script>

<template>
  <main>
    <NavbarContainer class="nav__isolator"/>
    <div class="journal">
      <ToolsContainer/>
      <AboutContainer/>
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
