<script setup>
import AddTask from '../buttons/AddTaskBtn.vue';
import AddTimeblock from '../buttons/AddTimeblockBtn.vue';
import DeleteTask from '../buttons/DeleteTaskBtn.vue';
import DeleteTimeblock from '../buttons/DeleteTimeblockBtn.vue';
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

  width: 15%;

  align-items: flex-start;
  margin: 5px;
  border: 2px solid gray;
  padding: 5px;
  min-height: 56vh;
  max-height: 56vh;
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