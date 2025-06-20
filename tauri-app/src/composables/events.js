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
      if (boxOjson.tasks === null) {
        events.tasks = [];
      } else {
        events.tasks = boxOjson.tasks;
      }
      if (boxOjson.timeblocks === null) {
        events.timeblocks = [];
      } else {
      events.timeblocks = boxOjson.timeblocks;
      }
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
      if (events.tasks === null) {
        events.tasks = [];
      } else {
        events.tasks = json.tasks;
      }
    } catch (e) {
      console.error("Error refreshing tasks:", e);
    }
  }

  const refreshTimeblocks = async () => {
    try {
      const res = await invoke('fetch_cspace', {
        userid: events.events_userId,
      });
      const json = JSON.parse(res);
      if (events.timeblocks === null) {
        events.timeblocks = [];
      } else {
        events.timeblocks = json.timeblocks;
      }
    } catch (e) {
      console.error("Error refreshing timeblocks:", e);
    }
  }

  return {
    events,
    unpackEvents,
    refreshTasks,
    refreshTimeblocks
  }
}