<script>
import NavbarContainer from '../components/containers/NavbarContainer.vue';
import PlanContainer from '../components/containers/PlanContainer.vue';

export default {
  components: {
    NavbarContainer,
    PlanContainer
  }
}
</script>

<template>
  <main>
    <NavbarContainer class="nav__isolator"/>
    <div class="plans">
      <PlanContainer/>
    </div>
  </main>
</template>

<style scoped>
.nav__isolator {
  display: flex;
}

.plans {
  display: flex;
}
</style>