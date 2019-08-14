<template>
  <div>
    <m-header></m-header>
    <left-menu></left-menu>
    <div class="container" :class="[indexShowUp ? 'index-up' : 'index-down']">
      <div class="main" id="main">
        <div class="content" id="content">
          <el-card>
            <div slot="header">
              <bread-crumb></bread-crumb>
            </div>
            <transition name="fade" mode="out-in">
              <router-view></router-view>
            </transition>
          </el-card>
        </div>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
import MHeader from './common/MHeader'
import LeftMenu from './common/LeftMenu'
import BreadCrumb from './common/BreadCrumb'

export default {
    components: {
        MHeader,
        LeftMenu,
        BreadCrumb
    },
    data() {
        return {
            indexShowUp: false
        }
    },
    methods: {
        indexDown() {
            this.indexShowUp = false
        },
        indexUp() {
            this.indexShowUp = true
        }
    },
    created() {
        this.$root.$on('indexDown', this.indexDown)
        this.$root.$on('indexUp', this.indexUp)
    }
}
</script>

<style scoped>
@import "../assets/css/main.css";
@import "../assets/css/admin.css";
@import "../assets/css/icon.css";

.container {
  position: fixed;
  top: 60px;
  bottom: 0;
  right: 0;
  z-index: -1;
  overflow: auto;
  width: calc(100% - 150px);
  background-color: #f5f5f5;
  transition: 0.3s width;
}

.index-up {
  z-index: 1;
}

.index-down {
  z-index: -1;
}

.main {
  width: 100%;
  height: calc(100% - 2px);
  padding: 0;
}

.content {
  clear: both;
  height: 100%;
  margin: 0;
}

.el-card {
  height: 100%;
  overflow: auto;
}

@media screen and (max-width: 600px) {
  .container {
    width: 100%;
  }
}
</style>
