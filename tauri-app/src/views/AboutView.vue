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

.about {
  display: flex;
}

.about__container {
  margin: 5px;
  border: 1px solid gray;
  padding: 0;
  max-height: 56vh;
  width: 85%
}
</style>
