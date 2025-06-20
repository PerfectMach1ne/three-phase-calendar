/* 
 * src/composables/events.js
*/
import { ref } from 'vue';
import { invoke } from '@tauri-apps/api/core';

const events = ref({
  tasks: [],
  timeblocks: [],
  events_userId: 0,
  cspaceId: 0
});

export function useEvents() {
  // Unboxing of the box'o'JSON of /api/login?id={uid} request to all /calendar events
  const unpackEvents = async (boxOjson) => {
    try {
      events.events_userId = boxOjson.userdata[0].user_id;
      events.cspaceId = boxOjson.userdata[0].calspace_id;
      events.tasks = boxOjson.tasks;
      events.timeblocks = boxOjson.timeblocks;
    } catch (e) {
      console.error("Error unpacking events:", e);
    }
  }

  const refreshTasks = async () => {
    try {
      const res = await invoke('fetch_cspace', {
        userid: events.events_userId,
      });
      const json = JSON.parse(res);
      events.tasks = json.tasks;
      console.log(events.tasks)
    } catch (e) {
      console.error("Error refreshin tasks:", e);
    }
  }

  const refreshTimeblocks = async () => {
    try {
      const res = await invoke('fetch_cspace', {
        userid: events.events_userId,
      });
      const json = JSON.parse(res);
      events.timeblocks = json.timeblocks;
    } catch (e) {
      console.error("Error refreshin timeblocks:", e);
    }
  }

  return {
    events,
    unpackEvents,
    refreshTasks,
    refreshTimeblocks
  }
}