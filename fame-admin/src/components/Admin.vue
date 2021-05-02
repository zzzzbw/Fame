<template>
  <div>
    <el-container class="container">
      <el-header height="60px">
        <nav-bar></nav-bar>
      </el-header>
      <el-container>
        <el-aside :class="[elAsideShow ? 'el-aside-show' : 'el-aside-hide']">
          <side-bar></side-bar>
        </el-aside>
        <el-main>
          <div :class="[indexShowUp ? 'index-up' : 'index-down']">
            <div id="main">
              <div class="bread-crumb-bar">
                <bread-crumb></bread-crumb>
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

<script type="text/ecmascript-6">
import NavBar from './common/NavBar'
import SideBar from './common/SideBar'
import BreadCrumb from './common/BreadCrumb'

export default {
  components: {
    NavBar,
    SideBar,
    BreadCrumb
  },
  data() {
    return {
      indexShowUp: false,
      elAsideShow: true
    }
  },
  methods: {
    indexDown() {
      this.indexShowUp = false
    },
    indexUp() {
      this.indexShowUp = true
    },
    sideBarCollapse() {
      this.elAsideShow = !this.elAsideShow
    },
    elAsideShowInit() {
      if (document.body.clientWidth < 600) {
        this.elAsideShow = false
      }
    }
  },
  created() {
    this.$root.$on('side-bar-collapse', this.sideBarCollapse)
    this.$root.$on('index-down', this.indexDown)
    this.$root.$on('index-up', this.indexUp)
    this.elAsideShowInit()
  }
}
</script>

<style scoped>
@import '../assets/css/main.css';
@import '../assets/css/admin.css';
@import '../assets/css/icon.css';

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
