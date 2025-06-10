<script setup>
import { inject } from 'vue';

const renderAddTask = inject('renderAddTask');

function toggle() {
  renderAddTask.value = !renderAddTask.value;
}
</script>

<template>
  <button class="tools__button" @click="toggle">Add task</button>
</template>