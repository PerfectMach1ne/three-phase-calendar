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
