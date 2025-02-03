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
      <div class="event__page__buttons">
        <button>Create</button>
        <button @click="cancel">Cancel</button>
      </div>
    </div>
  </div>
</template>