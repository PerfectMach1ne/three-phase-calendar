<script setup>
import AddTaskBtn from '../buttons/AddTaskBtn.vue';
import AddTimeblockBtn from '../buttons/AddTimeblockBtn.vue';
import DeleteTaskBtn from '../buttons/DeleteTaskBtn.vue';
import DeleteTimeblockBtn from '../buttons/DeleteTimeblockBtn.vue';
import ChangeTimeblockBtn from '../buttons/ChangeTimeblockBtn.vue';
import ChangeTaskBtn from '../buttons/ChangeTaskBtn.vue';
</script>

<template>
  <div class="tools__container scrollbar">
    <AddTaskBtn />
    <AddTimeblockBtn />
    <ChangeTaskBtn />
    <ChangeTimeblockBtn />
    <DeleteTaskBtn />
    <DeleteTimeblockBtn />
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