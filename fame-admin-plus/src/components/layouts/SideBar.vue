<template>
  <div class="side-bar">
    <el-menu
      background-color="#324157"
      text-color="#fff"
      active-text-color="#ffd04b"
      :router="true"
    >
      <el-menu-item index="/dashboard">
        <i class="el-icon-monitor"></i>
        <span>仪表盘</span>
      </el-menu-item>
      <el-menu-item index="/article">
        <i class="el-icon-document-copy"></i>
        <span>文章列表</span>
      </el-menu-item>
      <el-menu-item index="/comment">
        <i class="el-icon-chat-dot-round"></i>
        <span>评论列表</span>
      </el-menu-item>
      <el-menu-item index="/meta">
        <i class="el-icon-copy-document"></i>
        <span>标签/分类</span>
      </el-menu-item>
      <el-menu-item index="/media">
        <i class="el-icon-copy-document"></i>
        <span>媒体库</span>
      </el-menu-item>
      <el-menu-item index="/setting">
        <i class="el-icon-setting"></i>
        <span>网站设置</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script lang="ts"></script>

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
