<script>
export default {
  data() {
    return {

    }
  },
  methods: {
    enterView() {
      this.$router.push('/deletetask')
    }
  }
}
</script>

<template>
  <button class="tools__button" @click="enterView">Delete task</button>
</template>