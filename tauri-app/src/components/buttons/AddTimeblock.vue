<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

function enterView() {
  router.push('/addtimeblock')
}
</script>

<template>
  <button class="tools__button" @click="enterView">Add timeblock</button>
</template>