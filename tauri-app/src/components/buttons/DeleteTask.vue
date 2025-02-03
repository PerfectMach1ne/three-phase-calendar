<script>
export default {
  data() {
    return {
      
    }
  },
  methods: {
    enterView() {
      this.$router.push('/deletetaskpage')
    }
  }
}
</script>

<template>
  <button class="tools__button" @click="enterView">Delete task</button>
</template>