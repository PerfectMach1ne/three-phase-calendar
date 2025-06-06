<script setup>
import { created, ref } from 'vue';

defineProps({
  character: String
});
defineEmits([
  'todays-week'
]);

const displayChar = ref("");

created(() => {
  displayChar.value = character.value;
});
</script>

<template>
  <button
    class="change__week"
    v-html="this.displayChar"
    @click="$emit('todays-week')"
  ></button>
</template>