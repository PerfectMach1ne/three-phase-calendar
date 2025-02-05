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
        <button>Remove</button>
        <button @click="cancel">Cancel</button>
      </div>
    </div>
  </div>
</template>

<style>
.event__page__container {
  display: flex;
  justify-content: center;
}

.event__page__box {
  display: flex;
  flex-direction: column; /* ensure all is aligned in a column */
  align-content: center; /*  */
  align-items: center; /* centers item; justify-content does everything wrong, "in the column-specific way" */
  gap: 15px;
  margin: 5px;
  border: 1px solid gray;
  padding: 15px;
  min-height: 77.6vh;
  width: 35%;
  font-family: Arial, Helvetica, sans-serif;
  font-size: 1.2rem;
}

.event__page__buttons {
  display: inline-flex;
  flex-direction: row;
  gap: 15px;
}

div.event__page__buttons > button {
  margin: 0;
  border: 1px solid gray;
  padding: 10px 20px;
  border: none;
  border-radius: 12px;
  background-color: aquamarine;
  font-size: 1rem;
  color: #000;
}

div.event__page__buttons > button:hover {
  background-color: turquoise;
  transition: all 0.3s ease;
  cursor: pointer;
}

div.event__page__buttons > button:not(:hover) {
  transition: all 0.3s ease;
}
</style>