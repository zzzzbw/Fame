<template>
  <div class="nav-bar">
    <div class="collapse">
      <a @click="collapse"><span class="icon-th-list"></span></a>
    </div>
    <h3 class="title">Fame Dashboard</h3>
    <ul class="nav-bar-right">
      <li>
        <el-link @click="logout">
          <el-icon class="el-icon--left"><icon-back /></el-icon>退出
        </el-link>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
  import { Back as IconBack } from '@element-plus/icons-vue'
  import router from '~/router'
  import { ElMessage } from 'element-plus'
  import { RestResponse } from '~/types'
  import { Api } from '~/api'
  import { removeToken } from '~/utils'

  const logout = async () => {
    const resp = (await Api.logout()) as RestResponse<void>
    if (resp.success) {
      removeToken()
    } else {
      ElMessage.error(resp.msg || '登出失败!')
    }
    await router.push('/login')
  }
</script>

<style scoped>
  .nav-bar {
    width: 100%;
    height: 100%;
    display: inline-block;
    background-color: #fff;
    line-height: 60px;
    text-align: center;
    box-shadow: 0 2px 3px hsla(0, 0%, 7%, 0.1), 0 0 0 1px hsla(0, 0%, 7%, 0.1);
  }

  .collapse {
    display: none;
    float: left;
    width: 64px;
  }

  .title {
    margin: 0;
    display: inline-block;
  }

  .nav-bar-right {
    list-style: none;
    float: right;
    margin: 0 15px 0 0;
    color: #7f8c8d;
  }

  .nav-bar-right a {
    color: #7f8c8d;
    cursor: pointer;
    text-decoration: none;
  }

  @media screen and (max-width: 600px) {
    .collapse {
      display: block;
    }
  }
</style>
