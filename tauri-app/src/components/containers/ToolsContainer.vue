<script>
import AddReminder from '../buttons/AddReminder.vue';
import AddTask from '../buttons/AddTask.vue';
import DeleteReminder from '../buttons/DeleteReminder.vue';
import DeleteTask from '../buttons/DeleteTask.vue';

export default {
  components: { AddReminder, AddTask, DeleteReminder, DeleteTask },
  data() { return {} }
}
</script>

<template>
  <div class="tools__container scrollbar">
    <AddReminder />
    <AddTask />
    <DeleteReminder />
    <DeleteTask />
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
  max-height: calc(57vh - 1vh); /* same as above */
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