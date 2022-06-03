<template>
  <div>
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/' }">首页 </el-breadcrumb-item>
      <el-breadcrumb-item v-for="item in levelList" :key="item.name" :to="{ path: item.link }"
        >{{ item.title }}
      </el-breadcrumb-item>
    </el-breadcrumb>
  </div>
</template>

<script setup lang="ts">
  import { onMounted, reactive, watch } from 'vue'
  import router from '~/router'

  interface BreadCrumbItem {
    name: string
    title: string
    link: string
    parent: string
  }

  const config = [
    { name: 'ArticleList', title: '文章列表', link: '/article', parent: '' },
    {
      name: 'ArticleNew',
      title: '新建文章',
      link: '/article',
      parent: 'ArticleList'
    },
    {
      name: 'ArticleEdit',
      title: '编辑文章',
      link: '/article',
      parent: 'ArticleList'
    },
    { name: 'CommentList', title: '评论列表', link: '', parent: '' },
    { name: 'MetaList', title: '标签/分类', link: '', parent: '' },
    { name: 'MediaList', title: '媒体库', link: '', parent: '' },
    { name: 'Setting', title: '网站设置', link: '', parent: '' }
  ]

  const levelList = reactive<Array<BreadCrumbItem>>([])

  function initLevelList() {
    levelList.splice(0)
    let name = router.currentRoute.value.name
    if (!name) {
      return
    }
    getBreadcrumb(name)
    levelList.reverse()
  }

  function getBreadcrumb(name: string | symbol) {
    for (let i = 0; i < config.length; i++) {}
    config.forEach((c) => {
      if (c.name === name) {
        levelList.push(c)
        if (c.parent && c.parent !== '') {
          getBreadcrumb(c.parent)
        }
        return
      }
    })
  }

  onMounted(() => initLevelList())

  watch(
    () => router.currentRoute.value.name,
    () => initLevelList()
  )
</script>

<style scoped></style>
