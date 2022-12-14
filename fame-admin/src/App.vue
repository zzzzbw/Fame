<template>
  <el-config-provider :locale="locale">
    <router-view />
  </el-config-provider>
</template>

<script lang="ts">
  import { defineComponent } from 'vue'
  import zhCn from 'element-plus/lib/locale/lang/zh-cn'

  export default defineComponent({
    setup() {
      return {
        locale: zhCn
      }
    }
  })
</script>

<style>
  #app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
  }
  .element-plus-logo {
    width: 50%;
  }
</style>
