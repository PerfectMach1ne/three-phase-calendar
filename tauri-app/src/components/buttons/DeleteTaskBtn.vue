<script setup>
import { inject } from 'vue';

const renderDelTask = inject('renderDelTask');

function toggle() {
  renderDelTask.value = !renderDelTask.value;
}
</script>

<template>
  <button class="tools__button" @click="toggle">Delete task</button>
</template>