<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

function enterView() {
  router.push('/addtask')
}
</script>

<template>
  <button class="tools__button" @click="enterView">Add task</button>
</template>