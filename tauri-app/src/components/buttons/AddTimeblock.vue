<script>
export default {
  data() {
    return {

    }
  },
  methods: {
    enterView() {
      this.$router.push('/addtimeblock')
    }
  }
}
</script>

<template>
  <button class="tools__button" @click="enterView">Add timeblock</button>
</template>