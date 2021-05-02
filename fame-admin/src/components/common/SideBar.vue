<template>
  <div class="side-bar">
    <el-menu
      :default-active="activeMenu"
      background-color="#324157"
      text-color="#fff"
      active-text-color="#ffd04b"
      :router="true"
    >
      <el-menu-item index="/dashboard">
        <i class="el-icon-monitor"></i>
        <span slot="title">仪表盘</span>
      </el-menu-item>
      <el-menu-item index="/article">
        <i class="el-icon-document-copy"></i>
        <span slot="title">文章列表</span>
      </el-menu-item>
      <el-menu-item index="/comment">
        <i class="el-icon-chat-dot-round"></i>
        <span slot="title">评论列表</span>
      </el-menu-item>
      <el-menu-item index="/meta">
        <i class="el-icon-copy-document"></i>
        <span slot="title">标签/分类</span>
      </el-menu-item>
      <el-menu-item index="/media">
        <i class="el-icon-copy-document"></i>
        <span slot="title">媒体库</span>
      </el-menu-item>
      <el-menu-item index="/setting">
        <i class="el-icon-setting"></i>
        <span slot="title">网站设置</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script type="text/ecmascript-6">
export default {
  computed: {
    activeMenu() {
      const route = this.$route
      const {path} = route
      return path
    }
  }
}
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
