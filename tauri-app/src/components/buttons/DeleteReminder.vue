<script>
export default {
  data() {
    return {

    }
  },
  methods: {
    enterView() {
      this.$router.push('/deletereminderpage')
    }
  }
}
</script>

<template>
  <button class="tools__button" @click="enterView">Delete reminder</button>
</template>