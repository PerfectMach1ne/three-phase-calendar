<script>
export default {
  props: ['character'],
  emits: ['todays-week'],
  data() {
    return {
      displayChar: ""
    }
  },
  computed: {

  },
  methods: {
    
  },
  created() {
    this.displayChar = this.character;
  }
}
</script>

<template>
  <button
    class="change__week"
    v-html="this.displayChar"
    @click="$emit('todays-week')"
  ></button>
</template>