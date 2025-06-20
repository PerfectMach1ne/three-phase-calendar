<script setup>
import { inject } from 'vue';

const renderChangeTimeblock = inject('renderChangeTimeblock');

function toggle() {
  renderChangeTimeblock.value = !renderChangeTimeblock.value;
}
</script>

<template>
  <button class="tools__button" @click="toggle">Change timeblock</button>
</template>