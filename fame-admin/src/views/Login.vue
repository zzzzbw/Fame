<template>
  <div id="main" class="container">
    <div class="login-wrap">
      <h2 class="title">Fame Login</h2>
      <el-form ref="userFormRef" :model="userForm" :rules="rules" label-width="0px">
        <el-form-item prop="username">
          <el-input v-model="userForm.username" placeholder="用户名或邮箱"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="userForm.password"
            type="password"
            placeholder="密码"
            @keyup.enter="submitForm(userFormRef)"
          ></el-input>
        </el-form-item>
        <div class="login-btn">
          <el-button type="primary" @click="submitForm(userFormRef)">登录</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { reactive, ref } from 'vue'
  import router from '~/router'
  import { ElMessage, FormInstance, FormRules } from 'element-plus'
  import { RestResponse } from '~/types/common'
  import { Api } from '~/api'
  import { handleRestResponse, removeToken, setToken } from '~/utils'
  import { LoginResult } from '~/types/user'

  const userFormRef = ref<FormInstance>()

  const userForm = reactive({
    username: '',
    password: ''
  })

  const rules = reactive<FormRules>({
    username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
    password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
  })

  const submitForm = async (formEl: FormInstance | undefined) => {
    if (!formEl) return
    await formEl.validate(async (valid) => {
      if (!valid) {
        return
      }

      try {
        // 先清除之前的token
        removeToken()
        const resp = (await Api.login(userForm)) as RestResponse<LoginResult>
        handleRestResponse(resp, (userForm) => {
          // 存储token
          if (userForm.token && userForm.refreshToken) {
            setToken(userForm.token, userForm.refreshToken)
          } else {
            ElMessage.error('获取登录token异常!')
          }
        })
        await router.push('/')
        ElMessage.success('登录成功!')
      } catch (error) {
        console.error(error)
      }
    })
  }
</script>

<style scoped>
  .container {
    position: absolute;
    bottom: 0;
    top: 0;
    left: 0;
    right: 0;
    overflow: hidden;
    background-color: #f5f5f5;
  }

  .login-wrap {
    max-width: 330px;
    margin: 100px auto 0;
    background: #fff;
    border-radius: 5px;
    -webkit-border-radius: 5px;
    box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.2);
  }

  .title {
    margin: 0;
    padding: 25px 20px;
    text-align: center;
    background: #68dff0;
    border-radius: 5px 5px 0 0;
    -webkit-border-radius: 5px 5px 0 0;
    color: #fff;
    font-size: 20px;
    text-transform: uppercase;
    font-weight: 300;
  }

  .el-form {
    padding: 20px;
  }

  .login-btn button {
    width: 100%;
  }
</style>
