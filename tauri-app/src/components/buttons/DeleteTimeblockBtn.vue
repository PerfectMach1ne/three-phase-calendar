<script setup>
import { inject } from 'vue';

const renderDelTimeblock = inject('renderDelTimeblock');

function toggle() {
  renderDelTimeblock.value = !renderDelTimeblock.value;
}
</script>

<template>
  <button class="tools__button" @click="toggle">Delete timeblock</button>
</template>