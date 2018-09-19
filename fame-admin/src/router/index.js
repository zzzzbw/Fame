import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

const Error = (resolve) => {
  import('@/components/Error').then((module) => {
    resolve(module)
  })
}

const Login = (resolve) => {
  import('@/components/Login').then((module) => {
    resolve(module)
  })
}

const Admin = (resolve) => {
  import('@/components/Admin').then((module) => {
    resolve(module)
  })
}

const Dashboard = (resolve) => {
  import('@/components/page/Dashboard').then((module) => {
    resolve(module)
  })
}

const ArticleEdit = (resolve) => {
  import('@/components/page/Article').then((module) => {
    resolve(module)
  })
}

const ArticleList = (resolve) => {
  import('@/components/page/Articles').then((module) => {
    resolve(module)
  })
}

const CommentList = (resolve) => {
  import('@/components/page/Comments').then((module) => {
    resolve(module)
  })
}

const TagList = (resolve) => {
  import('@/components/page/Tags').then((module) => {
    resolve(module)
  })
}

const PageList = (resolve) => {
  import('@/components/page/Pages').then((module) => {
    resolve(module)
  })
}

const PageEdit = (resolve) => {
  import('@/components/page/Page').then((module) => {
    resolve(module)
  })
}

const Setting = (resolve) => {
  import('@/components/page/Setting').then((module) => {
    resolve(module)
  })
}

const router = new Router({
  mode: 'history',
  linkActiveClass: 'active',
  routes: [
    {
      path: '/',
      component: Admin,
      redirect: '/admin'
    },
    {
      path: '/admin/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/admin',
      name: 'Admin',
      component: Admin,
      redirect: '/admin/dashboard',
      children: [
        {
          path: 'dashboard',
          component: Dashboard
        },
        {
          path: 'article/publish/:id',
          component: ArticleEdit
        },
        {
          path: 'article/publish',
          component: ArticleEdit
        },
        {
          path: 'article',
          component: ArticleList
        },
        {
          path: 'comment',
          component: CommentList
        },
        {
          path: 'tag',
          component: TagList
        },
        {
          path: 'page',
          component: PageList
        },
        {
          path: 'page/publish/:id',
          component: PageEdit
        },
        {
          path: 'page/publish',
          component: PageEdit
        },
        {
          path: 'setting',
          component: Setting
        }
      ]
    },
    {
      path: '/error/:state/:message',
      component: Error
    },
    {
      path: '*',
      component: Error
    }
  ]
})

export default router
