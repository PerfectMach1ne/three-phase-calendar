<script setup>
import { computed, onMounted , ref } from 'vue';
import { invoke } from '@tauri-apps/api/core';
import ChangeWeek from './buttons/ChangeWeek.vue';
import TodaysWeek from './buttons/TodaysWeek.vue';

const weekdayBoxWidth = ref('');
const left = ref("&#10094;");
const right = ref("&#10095;");
const today = ref("&#10022;");

let currentDate = new Date();
const currentWeek = ref(18);
const currentFirstDayofweek = ref(0);
const currentLastDayofweek = ref(0);
const currentMonths = ref('Aug - Sep');
const currentYear = ref(2020);

const weekdayArray = computed(() => 
  ["Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"]
);
const monthNames = 
  ["January", "February", "March", "April", "May", "June",
   "July", "August", "September", "October", "November", "December"];

async function fetchCalSpace() {
  try {
    const res = await invoke('fetch_cspace', {
      userId: 14
    });
    console.log(res);
  } catch (error) {
    console.log(`Error: ${error}`);
  }
}

/*
 * Returns the ISO week of the date.
 */
Date.prototype.getWeek = function() {
  var date = new Date(this.getTime());
  date.setHours(0, 0, 0, 0);
  // Thursday in current week decides the year.
  date.setDate(date.getDate() + 3 - (date.getDay() + 6) % 7);
  // January 4 is always in week 1.
  var week1 = new Date(date.getFullYear(), 0, 4);
  // Adjust to Thursday in week 1 and count number of weeks from date to week1.
  return 1 + Math.round(((date.getTime() - week1.getTime()) / 86400000 - 3 + (week1.getDay() + 6) % 7) / 7);
}

function getTodaysWeek() {
  var date = currentDate;
  var weekNumeral = date.getWeek();

  return weekNumeral;
}

function getTodaysMonths() {
  fetchCalSpace();
  var date = currentDate;

  var weekFirstMonthday = date.getDate() - date.getDay();
  if (weekFirstMonthday == 0) weekFirstMonthday = 1;
  date = new Date(
    date.getFullYear(),
    date.getMonth(),
    weekFirstMonthday);
  var mondayMonth = date.getMonth();
  date = new Date(
    date.getFullYear(),
    date.getMonth(),
    weekFirstMonthday + 7);
  var sundayMonth = date.getMonth();
  
  if (mondayMonth === sundayMonth)
    return monthNames[mondayMonth];
  else
    return monthNames[mondayMonth] + ' - ' + monthNames[sundayMonth];
}

function getWeekMonthday(dayOffset) {
  var date = currentDate;

  var monthday = date.getDate() - date.getDay() + 1;
  date = new Date(date.getFullYear(), date.getMonth(), monthday + dayOffset);
  monthday = date.getDate();

  return monthday;
}

// ChangeWeek.vue events
function goToPastWeek() {
  var date = currentDate;
  var monthday = date.getDate() - date.getDay() + 1;
  date = new Date(date.getFullYear(), date.getMonth(), monthday - 7);
  currentDate = date;

  currentMonths.value = getTodaysMonths();
  currentFirstDayofweek.value = getWeekMonthday(0);
  currentLastDayofweek.value = getWeekMonthday(6);
  currentYear.value = currentDate.getFullYear();
  currentWeek.value = getTodaysWeek();

  console.log(currentWeek.value + ', ' + currentMonths.value + ', ' + currentFirstDayofweek.value + ', ' + currentLastDayofweek.value + ', ' + currentYear.value);
}

function goToFutureWeek() {
  var date = currentDate;
  var firstMonthDay = date.getDate() - date.getDay() + 1;
  date = new Date(date.getFullYear(), date.getMonth(), firstMonthDay + 7);
  currentDate = date;

  currentMonths.value = getTodaysMonths();
  currentFirstDayofweek.value = getWeekMonthday(0);
  currentLastDayofweek.value = getWeekMonthday(6);
  currentYear.value = currentDate.getFullYear();
  currentWeek.value = getTodaysWeek();

  console.log(currentWeek.value + ', ' + currentMonths.value + ', ' + currentFirstDayofweek.value + ', ' + currentLastDayofweek.value + ', ' + currentYear.value);
}

function goToTodaysWeek() {
  currentDate = new Date();
  currentDate.setHours(0, 0, 0, 0);

  currentMonths.value = getTodaysMonths();
  currentFirstDayofweek.value = getWeekMonthday(0);
  currentLastDayofweek.value = getWeekMonthday(6);
  currentYear.value = currentDate.getFullYear();
  currentWeek.value = getTodaysWeek();

  console.log(currentWeek.value + ', ' + currentMonths.value + ', ' + currentFirstDayofweek.value + ', ' + currentLastDayofweek.value + ', ' + currentYear.value);
}

onMounted(() => {
  currentDate.setHours(0, 0, 0, 0);

  currentMonths.value = getTodaysMonths();
  currentFirstDayofweek.value = getWeekMonthday(0);
  currentLastDayofweek.value = getWeekMonthday(6);
  currentYear.value = currentDate.getFullYear();
  currentWeek.value = getTodaysWeek();

  console.log(currentWeek.value + ', ' + currentMonths.value + ', ' + currentFirstDayofweek.value + ', ' + currentLastDayofweek.value + ', ' + currentYear.value);
  
  // Easy fix for yearmonth__display class element being sized correctly - steal weekday__box's width after render. >:)
  weekdayBoxWidth.value = document.getElementById('weekdaybox-width-source').offsetWidth + 'px';
});


</script>

<template>
  <div class="wrapper__big__calendar">
    <div class="yearmonth__display" :style="{ width: weekdayBoxWidth }">
      <div class="lazy__filler__box" :style="{
        width: '15px'
      }"></div>
      <ChangeWeek
        :character="left"
        @past-week="goToPastWeek()" />
      <TodaysWeek
        :character="today"
        @todays-week="goToTodaysWeek()" />
      <ChangeWeek
        :character="right"
        @future-week="goToFutureWeek()" />
      <!-- <span>&#10095;</span> move "right" button -->
      <div class="lazy__filler__box" :style="{
        width: '7px'
      }"></div>
      <span id="year__label">{{ currentYear }}</span>
      <span>&bull;</span>
      <span id="month__label">{{ currentMonths }}</span>
      <span>&bull;</span>
      <span id="week__label">Week {{ currentWeek }}</span>
    </div>
    <div class="long__task__display">
      <!-- TODO for that specific feature later; NOTE: it is to only appear when relevant -->
    </div>
    <ul class="weekday__box" id="weekdaybox-width-source">
      <li class="filler__cell"></li>
      <li v-for="i in 7" class="weekday__label">
        <p>
          {{ getWeekMonthday(i - 1) }}
        </p>
        <p>
          {{ weekdayArray[i - 1] }}
        </p>
      </li>
    </ul>
    <div class="calendar__with__hours">
      <div v-for="i in 24" v-bind:id="`${String(i - 1).padStart(2, '0') + ':00'}`" class="calendar__hour__row">
        <div class="hour__box">
          {{ String(i - 1).padStart(2, '0') + ':00' }}
        </div>
        <div v-for="j in 7" v-bind:id="`${weekdayArray[j - 1].substring(0,3)}${i - 1}`" class="hour__row__day">
          <!-- This is a hour grid box -->
        </div>
      </div>
    </div>
  </div>
</template>

<style>
.wrapper__big__calendar {
  margin: 0;
  padding: 0;
}

.yearmonth__display {
  display: inline-flex;
  align-items: center; /* prevents text from being aligned "annoyingly upwards" */
  gap: 15px;
  border: 1px solid lightgray;
  margin: 0;
  padding: 20px 0;
  content: 0;
  font-family: Arial, Helvetica, sans-serif;
  font-variant-caps: small-caps;
  font-size: 1.5rem;
  color: #424242;
}

.long__task__display {
  display: flex;
}

.weekday__box {
  display: inline-flex;
  list-style-type: none;
  padding: 0;
  margin: 0;
}

.filler__cell {
  border: 1px solid lightgray;
  margin: 0;
  padding: 0;
  content: 0;
  width: 60px;
}

.weekday__label {
  border: 1px solid lightgray;
  margin: 0;
  padding: 25px 34px;
  content: 0;
  min-width: 150px;
  text-align: center;
  vertical-align: middle;
  font-family: Arial, Helvetica, sans-serif;
  font-variant-caps: small-caps;
  font-size: 1.5rem;
  color: #767676;
}

.calendar__with__hours {
  --nothing-there: '';
}

.calendar__hour__row {
  display: inline-flex;
  padding: 0;
  margin: 0;
  content: 0;
  align-items: center;
  justify-content: center;
}

.hour__box {
  border: 1px solid lightgray;
  margin: 0;
  padding: 24.8px 0;
  content: 0;
  width: 60px;
  text-align: center;
  font-family: Arial, Helvetica, sans-serif;
  color: #767676;
}

.hour__row__day {
  border: 1px solid lightgray;
  margin: 0;
  padding: 34px;
  content: 0;
  min-width: 150px;
  text-align: center;
  vertical-align: middle;
}

button.change__week {
  margin: 0;
  border: 1px solid gray;
  padding: 10px 20px;
  border: none;
  border-radius: 12px;
  background-color: aquamarine;
  font-size: 1.2rem;
  color: #000;
}

button.change__week:hover {
  background-color: turquoise;
  transition: all 0.3s ease;
  cursor: pointer;
}

button.change__week:not(:hover) {
  transition: all 0.3s ease;
}
</style>