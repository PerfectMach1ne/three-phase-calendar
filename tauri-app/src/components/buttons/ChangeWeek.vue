<script>
export default {
  props: ['character'],
  emits: ['past-week', 'future-week'],
  data() {
    return {
      displayChar: "",
      buttonType: '',
    }
  },
  computed: {

  },
  methods: {
    
  },
  created() {
    this.displayChar = this.character;
    
    if ( Number(this.displayChar.slice(2,7)) == 10094 ) this.buttonType = 'left';
    else if ( Number(this.displayChar.slice(2,7)) == 10095 ) this.buttonType = 'right';
  }
}
</script>

<template>
  <button
    v-if="buttonType == 'left'"
    class="change__week"
    v-html="this.displayChar"
    @click="$emit('past-week')"></button>
  <button
    v-else-if="buttonType == 'right'"
    class="change__week"
    v-html="this.displayChar"
    @click="$emit('future-week')"></button>
  <p v-else>An error occurred while trying to display the button.</p> <!-- lol -->
</template>