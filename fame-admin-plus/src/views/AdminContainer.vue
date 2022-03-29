<template>
  <div>
    <el-container class="container full_screen">
      <el-header height="60px">
        <NavBar />
      </el-header>
      <el-container class="container">
        <el-aside :class="[elAsideShow ? 'el-aside-show' : 'el-aside-hide']">
          <SideBar />
        </el-aside>
        <el-main>
          <div :class="[indexShowUp ? 'index-up' : 'index-down']">
            <div id="main">
              <div class="bread-crumb-bar">
                <BreadCrumb />
              </div>
              <div class="content">
                <transition name="fade" mode="out-in">
                  <router-view></router-view>
                </transition>
              </div>
            </div>
          </div>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script lang="ts">
  import { defineComponent, ref } from 'vue'

  export default defineComponent({
    setup() {
      const indexShowUp = ref(false)
      const elAsideShow = ref(true)

      return {
        indexShowUp,
        elAsideShow
      }
    }
  })
</script>

<style scoped>
  @import '~/assets/css/main.scss';
  @import '~/assets/css/admin.scss';
  @import '~/assets/css/icon.scss';

  .index-up {
    z-index: 1;
  }

  .index-down {
    z-index: -1;
  }

  .el-header {
    padding: 0;
  }

  .el-main {
    padding: 0;
  }

  .el-aside {
    width: 150px !important;
    transition: all 0.2s;
  }

  .el-aside-show {
    width: 150px !important;
  }

  .el-aside-hide {
    width: 0 !important;
  }

  .container {
    display: flex;
  }

  .full_screen {
    min-height: calc(100vh);
  }

  .bread-crumb-bar {
    padding: 18px 20px;
    border-bottom: 1px solid #ebeef5;
    box-sizing: border-box;
  }

  .content {
    padding: 20px;
  }
</style>
