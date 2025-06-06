<script setup>
import { ref } from 'vue';

const props = defineProps(['character']);
const emits = defineEmits(['todays-week']);

const displayChar = ref("");

// onBeforeMount(() => {
displayChar.value = props.character;
// });
</script>

<template>
  <button
    class="change__week"
    v-html="displayChar"
    @click="$emit('todays-week')"
  ></button>
</template>