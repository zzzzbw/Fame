import Vue from "vue";
import Router from "vue-router";

Vue.use(Router);

const Error = resolve => {
  import("@/components/Error").then(module => {
    resolve(module);
  });
};

const Login = resolve => {
  import("@/components/Login").then(module => {
    resolve(module);
  });
};

const Admin = resolve => {
  import("@/components/Admin").then(module => {
    resolve(module);
  });
};

const Dashboard = resolve => {
  import("@/components/page/Dashboard").then(module => {
    resolve(module);
  });
};

const ArticleEdit = resolve => {
  import("@/components/page/Article").then(module => {
    resolve(module);
  });
};

const ArticleList = resolve => {
  import("@/components/page/Articles").then(module => {
    resolve(module);
  });
};

const CommentList = resolve => {
  import("@/components/page/Comments").then(module => {
    resolve(module);
  });
};

const TagList = resolve => {
  import("@/components/page/Tags").then(module => {
    resolve(module);
  });
};

const MediaList = resolve => {
  import("@/components/page/Medias").then(module => {
    resolve(module);
  });
};

const PageList = resolve => {
  import("@/components/page/Pages").then(module => {
    resolve(module);
  });
};

const PageEdit = resolve => {
  import("@/components/page/Page").then(module => {
    resolve(module);
  });
};

const Setting = resolve => {
  import("@/components/page/Setting").then(module => {
    resolve(module);
  });
};

const router = new Router({
  mode: "history",
  linkActiveClass: "active",
  routes: [
    {
      path: "/",
      component: Admin,
      redirect: "/admin"
    },
    {
      path: "/admin/login",
      name: "Login",
      component: Login
    },
    {
      path: "/admin",
      name: "Admin",
      component: Admin,
      redirect: "/admin/dashboard",
      children: [
        {
          path: "dashboard",
          name: "Dashboard",
          component: Dashboard
        },
        {
          path: "article/publish/:id",
          name: "ArticleEdit",
          component: ArticleEdit
        },
        {
          path: "article/publish",
          name: "ArticleNew",
          component: ArticleEdit
        },
        {
          path: "article",
          name: "ArticleList",
          component: ArticleList
        },
        {
          path: "comment",
          name: "CommentList",
          component: CommentList
        },
        {
          path: "tag",
          name: "TagList",
          component: TagList
        },
        {
          path: "media",
          name: "MediaList",
          component: MediaList
        },
        {
          path: "page",
          name: "PageList",
          component: PageList
        },
        {
          path: "page/publish/:id",
          name: "PageEdit",
          component: PageEdit
        },
        {
          path: "page/publish",
          name: "PageNew",
          component: PageEdit
        },
        {
          path: "setting",
          name: "Setting",
          component: Setting
        }
      ]
    },
    {
      path: "/error/:state/:message",
      name: "ErrorMessage",
      component: Error
    },
    {
      path: "*",
      name: "Error",
      component: Error
    }
  ]
});

export default router;
