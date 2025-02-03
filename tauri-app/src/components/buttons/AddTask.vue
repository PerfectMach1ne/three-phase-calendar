<script>
export default {
  data() {
    return {

    }
  },
  methods: {
    enterView() {
      this.$router.push('/addtaskpage')
    }
  }
}
</script>

<template>
  <button class="tools__button" @click="enterView">Add task</button>
</template>