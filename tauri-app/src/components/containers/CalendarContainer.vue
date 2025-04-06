<script>
import BigCalendar from '../BigCalendar.vue';

export default {
  components: {
    BigCalendar
  }
}
</script>

<template>
  <div class="calendar__container scrollbar-vertical">
    <BigCalendar/>
  </div>
</template>

<style>
.calendar__container {
  margin: 5px;
  border: 1px solid gray;
  padding: 5px;
  max-height: 56vh;
  width: 85%
}
</style>