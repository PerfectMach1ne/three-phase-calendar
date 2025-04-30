<script>
export default {
  data() {
    return {
      
    }
  },
  methods: {
    enterView() {
      this.$router.push('/deletetimeblock')
    }
  }
}
</script>

<template>
  <button class="tools__button" @click="enterView">Delete timeblock</button>
</template>