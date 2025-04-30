import { createRouter, createWebHistory } from 'vue-router'
import CalendarView from '../views/CalendarView.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'calendar',
      component: CalendarView
    },
    {
      path: '/journal',
      name: 'journal',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/JournalView.vue')
    },
    {
      path: '/plan',
      name: 'plan',
      component: () => import('../views/PlanView.vue')
    },
    {
      path: '/addtask',
      name: 'addreminder',
      component: () => import('../views/button-views/AddTaskView.vue')
    },
    {
      path: '/deletetask',
      name: 'deletereminder',
      component: () => import('../views/button-views/DeleteTaskView.vue')
    },
    {
      path: '/addtimeblock',
      name: 'addtask',
      component: () => import('../views/button-views/AddTimeblockView.vue')
    },
    {
      path: '/deletetimeblock',
      name: 'deletetask',
      component: () => import('../views/button-views/DeleteTimeblockView.vue')
    },
  ]
})

export default router;
