<script setup>
import { created, ref } from 'vue';

defineProps({
  character: String
});
defineEmits([
  'past-week', 'future-week'
]);

const displayChar = ref("");
const buttonType = ref("");

created(() => {
  displayChar.value = character.value;
  
  if ( Number(displayChar.value.slice(2,7)) == 10094 ) buttonType.value = 'left';
  else if ( Number(displayChar.value.slice(2,7)) == 10095 ) buttonType.value = 'right';
});
</script>

<template>
  <button
    v-if="buttonType == 'left'"
    class="change__week"
    v-html="this.displayChar"
    @click="$emit('past-week')"></button>
  <button
    v-else-if="buttonType == 'right'"
    class="change__week"
    v-html="this.displayChar"
    @click="$emit('future-week')"></button>
  <p v-else>An idiotic error occurred while trying to display the button.</p> <!-- lol -->
</template>