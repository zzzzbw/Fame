import Vue from 'vue'
import Router from 'vue-router'

import Error from '../components/Error'
import Admin from '../components/Admin'
import Login from '../components/Login'
import Dashboard from '../components/page/Dashboard'
import PostEdit from '../components/page/Post'
import PostList from '../components/page/Posts'
import CommentList from '../components/page/Comments'
import MetaList from '../components/page/Metas'
import MediaList from '../components/page/Medias'
import NoteList from '../components/page/Notes'
import NoteEdit from '../components/page/Note'
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
      component: Login
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
          component: Dashboard
        },
        {
          path: 'post/publish/:id',
          name: 'PostEdit',
          component: PostEdit
        },
        {
          path: 'post/publish',
          name: 'PostNew',
          component: PostEdit
        },
        {
          path: 'post',
          name: 'PostList',
          component: PostList
        },
        {
          path: 'comment',
          name: 'CommentList',
          component: CommentList
        },
        {
          path: 'meta',
          name: 'MetaList',
          component: MetaList
        },
        {
          path: 'media',
          name: 'MediaList',
          component: MediaList
        },
        {
          path: 'note',
          name: 'NoteList',
          component: NoteList
        },
        {
          path: 'note/publish/:id',
          name: 'NoteEdit',
          component: NoteEdit
        },
        {
          path: 'note/publish',
          name: 'NoteNew',
          component: NoteEdit
        },
        {
          path: 'setting',
          name: 'Setting',
          component: Setting
        }
      ]
    },
    {
      path: '/error/:state/:message',
      name: 'ErrorMessage',
      component: Error
    },
    {
      path: '*',
      name: 'Error',
      component: Error
    }
  ]
})

export default router
