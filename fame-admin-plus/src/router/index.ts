import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
const routes: Array<RouteRecordRaw> = [
  {
    path: '/:pathMatch(.*)*',
    redirect: '/error/404'
  },
  {
    path: '/error/404',
    component: () => import('~/views/Error.vue')
  },
  {
    path: '/login',
    component: () => import('~/views/Login.vue')
  },
  {
    path: '/',
    name: 'Admin',
    component: () => import('~/views/AdminContainer.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('~/views/Dashboard.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router