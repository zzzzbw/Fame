<template>
  <div>
    <m-header></m-header>
    <left-menu></left-menu>
    <div class="container" :class="[ indexShowUp ? 'index-up':'index-down' ]">
      <div class="main" id="main">
        <div class="content" id="content">
          <transition name="move" mode="out-in">
            <router-view></router-view>
          </transition>
        </div>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import MHeader from './common/MHeader'
  import LeftMenu from './common/LeftMenu'

  export default {
    components: {
      MHeader,
      LeftMenu
    },
    data () {
      return {
        indexShowUp: false
      }
    },
    methods: {
      indexDown () {
        this.indexShowUp = false
      },
      indexUp () {
        this.indexShowUp = true
      }
    },
    created () {
      document.title = 'Fame Admin'
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
    height: 100%;
    padding: 0;
  }

  .content {
    clear: both;
    margin: 30px;
  }

  @media screen and (max-width: 600px) {
    .container {
      width: 100%;
    }
  }
</style>
