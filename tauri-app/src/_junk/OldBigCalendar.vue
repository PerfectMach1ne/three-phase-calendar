<script>
export default {
  data() {
    return {
      hourBoxWidth: 0,
      hourLabelHeight: 0,
      weekdayLabelWidth: 0
    }
  },
  computed: {
    weekdayArray() {
      return ["Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"];
    }
  },
  mounted() {
    //  
    this.hourBoxWidth = document.getElementById("hour__label").offsetWidth;
    this.hourLabelHeight = document.getElementById("hour__label").offsetHeight;
    this.weekdayLabelWidth = document.getElementById("weekday__label").offsetWidth;
    document.documentElement.style.setProperty(`--hour-box-width`, `${this.hourBoxWidth}px`);
    document.documentElement.style.setProperty(`--hour-label-height`, `${this.hourLabelHeight + 0.19}px`);
    document.documentElement.style.setProperty(`--weekday-label-width`, `${this.weekdayLabelWidth - 1.05}px`);
  }
}
</script>

<template>
  <ul class="weekday__box">
    <li id="filler__box"></li>
    <li v-for="i in 7" id="weekday__label">
      <p>
        {{ weekdayArray[i - 1] }}
      </p>
    </li>
  </ul>
  
  <div class="calendar__with__hours">
    <ul id="hour__box">
      <li v-for="i in 23" id="hour__label">
        <p>
          {{ String(i - 1).padStart(2, '0') + ':00' }}
        </p>
      </li>
      <li :style="{ 
        border: '1px',
        'border-color': 'lightgray',
        'border-style': 'solid solid solid none',
        width: 'fit-content',
        'padding': '10px',
        'z-index': 0
      }">
        <p>
          {{ String(23).padStart(2, '0') + ':00' }}
        </p>
      </li>
    </ul>

    <div id="seven__day__cal">
      <div id="event__box" v-for="i in 7">
        <div :id="weekdayArray[i - 1]" :style="{
            padding: 0,
            margin: 0,
            width: 'var(--weekday-label-width)'
        }"></div>
      </div>
      <div class="table__loop" v-for="i in 24" v-bind:id="`${String(i - 1).padStart(2, '0') + ':00'}`">
        <div class="table__row">
          <div class="table__cell" :id="`mon${i - 1}`"></div>
          <div class="table__cell" :id="`tue${i - 1}`"></div>
          <div class="table__cell" :id="`wed${i - 1}`"></div>
          <div class="table__cell" :id="`thu${i - 1}`"></div>
          <div class="table__cell" :id="`fri${i - 1}`"></div>
          <div class="table__cell" :id="`sat${i - 1}`"></div>
          <div class="table__cell" :id="`sun${i - 1}`"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<style>
:root {
  --hour-box-width: '-';
  --hour-label-height: '-';
  --weekday-label-width: '-';
}

#event__box {
  /* height: 1709.6px; */
  z-index: 1;
}

#seven__day__cal {
  margin: 0;
  z-index: 0;
}

.table__loop {
  padding: 0;
  margin: 0;
  height: var(--hour-label-height);
}

.table__row {
  display: inline-flex;
}

.table__cell {
  border: 1px solid lightgray;
  padding: 0;
  margin: 0;
  height: var(--hour-label-height);
  width: var(--weekday-label-width);
  border-style: solid solid none none;
}

.weekday__box {
  display: flex;
  flex-direction: row;
  list-style-type: none;
  padding: 0;
  margin: 0;
  width: fit-content; 
  height: fit-content;
  /* font-family: 'Fira Sans', sans-serif; */
  font-variant-caps: small-caps;
  z-index: 0;
}

#weekday__label {
  border: 1px solid lightgray;
  border-style: none solid none none;
  /* width: fit-content; */
  min-width: 110px;
  max-height: 90px;
  padding: 28px;
  text-align: center;
  z-index: 0;
}

#filler__box {
  border: 1px solid lightgray;
  padding: 28px;
  margin: 0;
  max-width: var(--hour-box-width);
  border-style: none solid none none;
  /* min-width: 50px;
  max-height: 90px; */
  z-index: 0;
}

#hour__box {
  list-style-type: none;
  padding: 0;
  margin: 0;
  width: fit-content; /* Prevents the hour box from taking up insanely large virtual space */
  height: fit-content;
  z-index: 0;
}

#hour__label {
  border: 1px;
  border-color: lightgray;
  border-style: solid solid none none;
  width: fit-content;
  padding: 10px;
  /* height: 50px; */
  z-index: 0;
}

.calendar__with__hours {
  display: flex;
  flex-direction: row;
  flex-wrap: nowrap;
}
</style>