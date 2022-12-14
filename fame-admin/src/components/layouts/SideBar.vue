<template>
  <div class="side-bar">
    <el-menu
      background-color="#324157"
      text-color="#fff"
      active-text-color="#ffd04b"
      :router="true"
    >
      <el-menu-item index="/dashboard">
        <el-icon><DataAnalysis /></el-icon>
        <span>仪表盘</span>
      </el-menu-item>
      <el-menu-item index="/article">
        <el-icon><Tickets /></el-icon>
        <span>文章列表</span>
      </el-menu-item>
      <el-menu-item index="/comment">
        <el-icon><ChatSquare /></el-icon>
        <span>评论列表</span>
      </el-menu-item>
      <el-menu-item index="/meta">
        <el-icon><PriceTag /></el-icon>
        <span>标签/分类</span>
      </el-menu-item>
      <el-menu-item index="/media">
        <el-icon><Picture /></el-icon>
        <span>媒体库</span>
      </el-menu-item>
      <el-menu-item index="/setting">
        <el-icon><Setting /></el-icon>
        <span>网站设置</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script setup lang="ts">
  import {
    DataAnalysis,
    Tickets,
    ChatSquare,
    PriceTag,
    Picture,
    Setting
  } from '@element-plus/icons-vue'
</script>

<style>
  .el-submenu .el-menu-item {
    min-width: 0;
  }
</style>

<style scoped>
  .side-bar {
    width: 100%;
    height: 100%;
    background-color: #324157;
    box-shadow: 0 2px 3px hsla(0, 0%, 7%, 0.1), 0 0 0 1px hsla(0, 0%, 7%, 0.1);
    transition: 0.3s left;
  }

  .side-bar .el-menu {
    border-right: none;
  }
</style>
