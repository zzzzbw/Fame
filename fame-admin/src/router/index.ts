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
      },
      {
        path: '/article',
        name: 'ArticleList',
        component: () => import('~/views/ArticleList.vue')
      },
      {
        path: '/article/edit/:id',
        name: 'ArticleEdit',
        component: () => import('~/views/Article.vue')
      },
      {
        path: '/article/edit',
        name: 'ArticleNew',
        component: () => import('~/views/Article.vue')
      },
      {
        path: '/comment',
        name: 'Comment',
        component: () => import('~/views/CommentList.vue')
      },
      {
        path: '/meta',
        name: 'Meta',
        component: () => import('~/views/Meta.vue')
      },
      {
        path: '/setting',
        name: 'SettingList',
        component: () => import('~/views/Setting.vue')
      },
      {
        path: '/media',
        name: 'MediaList',
        component: () => import('~/views/MediaList.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory('/admin/'),
  routes
})

export default router
