import { ref, computed } from 'vue';
import { invoke } from '@tauri-apps/api/tauri';

const events = ref({
  tasks: [],
  timeblocks: [],
  userdata: null
});

export function useEvents() {
  const fetchEvents = async (userId) => {
    try {
      const data = await invoke('fetch_cspace', { userId });
      events.value = JSON.parse(data);
      return events.value;
    } catch (error) {
      console.error("Event fetch failed:", error);
      throw error;
    }
  };

  const addTask = (task) => {
    events.value.tasks.push(task);
    // Optional: Immediately sync with backend
  };

  const updateEvent = (event) => {
    const store = event.viewtype.includes('task') ? 'tasks' : 'timeblocks';
    const index = events.value[store].findIndex(e => e.hashcode === event.hashcode);
    if (index >= 0) {
      events.value[store][index] = event;
    }
  };

  const currentUser = computed(() => events.value.userdata?.[0]);

  return {
    events,
    fetchEvents,
    addTask,
    updateEvent,
    currentUser
  };
}


<script setup>
import { useAuth, useEvents } from '@/composables';

const { jwtToken, loadToken } = useAuth();
const { events, fetchEvents } = useEvents();

onMounted(async () => {
  await loadToken();
  if (jwtToken.value) {
    await fetchEvents(14); // Or get user ID from auth
  }
});
</script>

<template>
  <div v-if="events.userdata">
    <h2>Welcome user {{ events.userdata.userid }}</h2>
    <div v-for="task in events.tasks" :key="task.hashcode">
      {{ task.name }} - {{ task.datetime }}
    </div>
  </div>
</template>


/ src/composables/events.js
import { ref } from 'vue';
import { invoke } from '@tauri-apps/api';

export const useEvents = () => {
  const events = ref([]);

  const fetchEvents = async (userId) => {
    try {
      events.value = await invoke('fetch_events', { userId });
    } catch (error) {
      console.error("🌀 Event fetch failed:", error);
    }
  };

  return { events, fetchEvents };
};
