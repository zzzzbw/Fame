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

const PostEdit = resolve => {
  import("@/components/page/Post").then(module => {
    resolve(module);
  });
};

const PostList = resolve => {
  import("@/components/page/Posts").then(module => {
    resolve(module);
  });
};

const CommentList = resolve => {
  import("@/components/page/Comments").then(module => {
    resolve(module);
  });
};

const MetaList = resolve => {
  import("@/components/page/Metas").then(module => {
    resolve(module);
  });
};

const MediaList = resolve => {
  import("@/components/page/Medias").then(module => {
    resolve(module);
  });
};

const NoteList = resolve => {
  import("@/components/page/Notes").then(module => {
    resolve(module);
  });
};

const NoteEdit = resolve => {
  import("@/components/page/Note").then(module => {
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
  base: "admin",
  linkActiveClass: "active",
  routes: [
    {
      path: "/login",
      name: "Login",
      component: Login
    },
    {
      path: "/",
      name: "Admin",
      component: Admin,
      redirect: "/dashboard",
      children: [
        {
          path: "dashboard",
          name: "Dashboard",
          component: Dashboard
        },
        {
          path: "post/publish/:id",
          name: "PostEdit",
          component: PostEdit
        },
        {
          path: "post/publish",
          name: "PostNew",
          component: PostEdit
        },
        {
          path: "post",
          name: "PostList",
          component: PostList
        },
        {
          path: "comment",
          name: "CommentList",
          component: CommentList
        },
        {
          path: "meta",
          name: "MetaList",
          component: MetaList
        },
        {
          path: "media",
          name: "MediaList",
          component: MediaList
        },
        {
          path: "note",
          name: "NoteList",
          component: NoteList
        },
        {
          path: "note/publish/:id",
          name: "NoteEdit",
          component: NoteEdit
        },
        {
          path: "note/publish",
          name: "NoteNew",
          component: NoteEdit
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
