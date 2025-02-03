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
  <NavbarContainer class="nav__isolator"/>
  <div class="about">
    <ToolsContainer/>
    <AboutContainer/>
  </div>
</template>

<style scoped>
.nav__isolator {
  display: flex;
}
</style>
