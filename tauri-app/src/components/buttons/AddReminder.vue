<script>
export default {
  data() {
    return {

    }
  },
  methods: {
    enterView() {
      this.$router.push('/addreminderpage')
    }
  }
}
</script>

<template>
  <button class="tools__button" @click="enterView">Add reminder</button>
</template>