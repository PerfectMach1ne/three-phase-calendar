<script setup>
import { ref } from 'vue';
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css';

</script>

<template>
  <div class="journal__container scrollbar">
    <QuillEditor theme="snow" toolbar="full"></QuillEditor>
  </div>
</template>

<style>
.journal__container {
  margin: 5px;
  border: 2px solid gray;
  padding: 5px;
  min-height: 56vh;
  max-height: 56vh;
  width: 85%
}
</style>