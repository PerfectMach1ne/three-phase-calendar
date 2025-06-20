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
      consol.err("", e);
    }
  }

  return {
    events,
    unpackEvents
  }
}