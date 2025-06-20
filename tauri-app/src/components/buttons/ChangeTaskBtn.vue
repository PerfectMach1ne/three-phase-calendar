<script setup>
import { inject } from 'vue';

const renderChangeTask = inject('renderChangeTask');

function toggle() {
  renderChangeTask.value = !renderChangeTask.value;
}
</script>

<template>
  <button class="tools__button" @click="toggle">Change task</button>
</template>