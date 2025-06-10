<script setup>
import { inject, ref } from 'vue';

const renderDelTimeblock = inject('renderDelTimeblock');

function cancel() {
  renderDelTimeblock.value = !renderDelTimeblock.value;
}
</script>

<template>
  <div class="event__page__box">
    <div class="event__page__buttons">
      <button>Remove</button>
      <button @click="cancel">Cancel</button>
    </div>
  </div>
</template>

<style scoped>
.event__page__box {
  z-index: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  position: fixed;
  top: 30%;
  left: 30%;
  right: 30%;
  max-width: 35%;
  min-height: 150px; 
  height: 150px;
  gap: 15px;
  margin: 5px;
  padding: 15px;
  border: 1px solid gray;
  font-family: Arial, Helvetica, sans-serif;
  font-size: 1.2rem;
}
</style>