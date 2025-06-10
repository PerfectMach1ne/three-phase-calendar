<script setup>
import { inject } from 'vue';

const renderAddTimeblock = inject('renderAddTimeblock');

function toggle() {
  renderAddTimeblock.value = !renderAddTimeblock.value;
}
</script>

<template>
  <button class="tools__button" @click="toggle">Add timeblock</button>
</template>