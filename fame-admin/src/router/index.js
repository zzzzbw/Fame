import Vue from 'vue'
import Router from 'vue-router'

import Error from '../components/Error'
import Admin from '../components/Admin'
import Login from '../components/Login'
import Dashboard from '../components/page/Dashboard'
import ArticleEdit from '../components/page/Article'
import ArticleList from '../components/page/ArticleList'
import CommentList from '../components/page/CommentList'
import MetaList from '../components/page/MetaList'
import MediaList from '../components/page/MediaList'
import Setting from '../components/page/Setting'

Vue.use(Router)

const router = new Router({
  mode: 'history',
  base: 'admin',
  linkActiveClass: 'active',
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: Login,
    },
    {
      path: '/',
      name: 'Admin',
      component: Admin,
      redirect: '/dashboard',
      children: [
        {
          path: 'dashboard',
          name: 'Dashboard',
          component: Dashboard,
        },
        {
          path: 'article/publish/:id',
          name: 'ArticleEdit',
          component: ArticleEdit,
        },
        {
          path: 'article/publish',
          name: 'ArticleNew',
          component: ArticleEdit,
        },
        {
          path: 'article',
          name: 'ArticleList',
          component: ArticleList,
        },
        {
          path: 'comment',
          name: 'CommentList',
          component: CommentList,
        },
        {
          path: 'meta',
          name: 'MetaList',
          component: MetaList,
        },
        {
          path: 'media',
          name: 'MediaList',
          component: MediaList,
        },
        {
          path: 'setting',
          name: 'Setting',
          component: Setting,
        },
      ],
    },
    {
      path: '/error/:state/:message',
      name: 'ErrorMessage',
      component: Error,
    },
    {
      path: '*',
      name: 'Error',
      component: Error,
    },
  ],
})

export default router
