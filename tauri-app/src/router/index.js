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
      path: '/planner',
      name: 'planner',
      component: () => import('../views/PlannerView.vue')
    },
    {
      path: '/addreminderpage',
      name: 'addreminder',
      component: () => import('../views/button-views/AddReminderView.vue')
    },
    {
      path: '/addtaskpage',
      name: 'addtask',
      component: () => import('../views/button-views/AddTaskView.vue')
    },
    {
      path: '/deletereminderpage',
      name: 'deletereminder',
      component: () => import('../views/button-views/DeleteReminderView.vue')
    },
    {
      path: '/deletetaskpage',
      name: 'deletetask',
      component: () => import('../views/button-views/DeleteTaskView.vue')
    },
  ]
})

export default router;
