<script>
// https://vuejs.org/guide/essentials/component-basics.html#defining-a-component
// https://vuejs.org/examples/#simple-component
import MiniCalendar from '../MiniCalendar.vue';
import TaskBar from '../TaskBar.vue';

export default {
  components: {
    MiniCalendar,
    TaskBar
  }
}
</script>

<template>
  <div class="navbar__container">
    <p>NavbarContainer</p>
    <MiniCalendar/>
    <TaskBar/>
  </div>
</template>

<style>
.navbar__container {
  margin: 5px;
  border: 1px solid gray;
  padding: 5px;
  width: revert;
  height: 20vh; 
  display: inline-flex;
  flex-direction: row;
  gap: 25px;
}
</style>