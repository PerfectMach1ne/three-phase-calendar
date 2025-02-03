<script>
export default {
  props: [],
  emits: [],
  data() {
    return {

    }
  },
  methods: {
    cancel() {
      this.$router.push('/')
    } 
  }
}
</script>

<template>
  <div class="event__page__container">
    <div class="event__page__box">
      <span>
        <label for="reminder-title-input">Title: </label>
        <input type="text" id="reminder-title-input" name="reminder-title-input">
      </span>
      <span>
        <label for="reminder-datetime-input">Date & time: </label>
        <input type="datetime-local" id="reminder-datetime-input" name="reminder-datetime-input">
      </span>
      <!-- <span>
        <label for="hours-choice">Hours: </label>
        <input list="hours-list" id="hours-choice" name="hours-choice">
        <datalist id="hours-list">
          <option value=""></option>
          <option v-for="i in 24" v-bind:value="`${String(i - 1).padStart(2, '0') + ':00'}`"></option>
        </datalist>
      </span> -->
      <div class="event__page__buttons">
        <button>Create</button>
        <button @click="cancel">Cancel</button>
      </div>
      
    </div>
  </div>
</template>