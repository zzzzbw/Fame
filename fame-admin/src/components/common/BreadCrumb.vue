<template>
  <div>
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/admin/dashboard' }"
        >首页
      </el-breadcrumb-item>
      <el-breadcrumb-item
        v-for="item in levelList"
        :key="item.name"
        :to="{ path: item.link }"
        >{{ item.title }}
      </el-breadcrumb-item>
    </el-breadcrumb>
  </div>
</template>

<script>
const config = [
  { name: "PostList", title: "文章列表", link: "/admin/post" },
  {
    name: "PostNew",
    title: "新建文章",
    parent: "PostList"
  },
  {
    name: "PostEdit",
    title: "编辑文章",
    parent: "PostList"
  },
  { name: "CommentList", title: "评论列表" },
  { name: "MetaList", title: "标签/分类" },
  { name: "MediaList", title: "媒体库" },
  { name: "NoteList", title: "页面列表", link: "/admin/note" },
  { name: "NoteNew", title: "新建页面", parent: "NoteList" },
  { name: "NoteEdit", title: "编辑页面", parent: "NoteList" },
  { name: "Setting", title: "网站设置" }
];

export default {
  name: "BreadCrumb",
  data() {
    return {
      levelList: []
    };
  },
  methods: {
    initLevelList() {
      this.levelList = [];
      if (!this.$route.name) {
        return;
      }
      this.getBreadcrumb(this.$route.name);
      this.levelList.reverse();
    },
    getBreadcrumb(name) {
      let breadCrumbItem = null;
      config.forEach(c => {
        if (c.name === name) {
          breadCrumbItem = c;
        }
      });
      if (null === breadCrumbItem) {
        return;
      }
      this.levelList.push(breadCrumbItem);
      if (breadCrumbItem.parent && breadCrumbItem.parent !== "") {
        this.getBreadcrumb(breadCrumbItem.parent);
      }
    }
  },
  watch: {
    $route(route) {
      // if you go to the redirect page, do not update the breadcrumbs
      if (route.path.startsWith("/redirect/")) {
        return;
      }
      this.initLevelList();
    }
  },
  created() {
    this.initLevelList();
  }
};
</script>

<style scoped></style>
