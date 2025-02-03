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
