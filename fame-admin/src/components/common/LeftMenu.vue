<template>
  <div
    :class="[showLeftMenu ? 'left-menu-show' : 'left-menu-hide']"
    class="left-menu"
  >
    <el-menu
      :default-active="activeMenu"
      background-color="#324157"
      text-color="#fff"
      active-text-color="#ffd04b"
      :router="true"
      @select="mobileToggle"
    >
      <el-menu-item index="/dashboard">
        <i class="el-icon-monitor"></i>
        <span slot="title">仪表盘</span>
      </el-menu-item>
      <el-menu-item index="/post">
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
      <el-menu-item index="/note">
        <i class="el-icon-document"></i>
        <span slot="title">页面列表</span>
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
    data() {
        return {
            showLeftMenu: false
        }
    },
    computed: {
        activeMenu() {
            const route = this.$route
            const {path} = route
            return path
        }
    },
    methods: {

        toggleLeftMenu() {
            this.showLeftMenu = !this.showLeftMenu
        },
        mobileToggle() {
            if (document.body.clientWidth < 600) {
                this.showLeftMenu = false
            }
        }
    },
    created() {
        this.$root.$on('collapse', this.toggleLeftMenu)
    }
}
</script>

<style>
.el-submenu .el-menu-item {
  min-width: 0px;
}
</style>

<style scoped>
.left-menu {
  position: fixed;
  top: 60px;
  bottom: 0;
  left: 0;
  width: 150px;
  background-color: #324157;
  box-shadow: 0 2px 3px hsla(0, 0%, 7%, 0.1), 0 0 0 1px hsla(0, 0%, 7%, 0.1);
  transition: 0.3s left;
}

.left-menu .el-menu {
  border-right: none;
}

.left-menu-show {
  left: 0;
}

.left-menu-hide {
  left: -151px;
}

@media screen and (min-width: 600px) {
  .left-menu {
    left: 0;
  }
}
</style>
