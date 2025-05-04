<script setup>
import AddTask from '../buttons/AddTask.vue';
import AddTimeblock from '../buttons/AddTimeblock.vue';
import DeleteTask from '../buttons/DeleteTask.vue';
import DeleteTimeblock from '../buttons/DeleteTimeblock.vue';
</script>

<template>
  <div class="tools__container scrollbar">
    <AddTask />
    <AddTimeblock />
    <DeleteTask />
    <DeleteTimeblock />
  </div>
</template>

<style>
.tools__container {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  /* align-content: flex-start; */
  align-items: flex-start;
  margin: 5px;
  border: 1px solid gray;
  padding: 5px;
  width: 15%;
  max-height: calc(57vh - 1vh);
}

/* Buttons for adding/deleting events in tools bar */
button.tools__button {
  margin: 5px;
  border: 1px solid gray;
  padding: 10px 20px;
  border: none;
  border-radius: 12px;
  background-color: aquamarine;
  font-size: 1.2rem;
  color: #000;
}

button.tools__button:hover {
  background-color: turquoise;
  transition: all 0.3s ease;
  cursor: pointer;
}

button.tools__button:not(:hover) {
  transition: all 0.3s ease;
}
</style>