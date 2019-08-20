<template>
  <div class="container" id="main">
    <div class="login-wrap">
      <h2 class="title">Fame Login</h2>
      <el-form
        :model="userForm"
        :rules="rules"
        ref="userForm"
        label-width="0px"
      >
        <el-form-item prop="username">
          <el-input
            v-model="userForm.username"
            placeholder="用户名或邮箱"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            type="password"
            placeholder="密码"
            v-model="userForm.password"
            @keyup.enter.native="submitForm('userForm')"
          ></el-input>
        </el-form-item>
        <div class="login-btn">
          <el-button type="primary" @click="submitForm('userForm')"
            >登录
          </el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
export default {
    data: function () {
        return {
            userForm: {
                username: '',
                password: ''
            },
            rules: {
                username: [
                    {required: true, message: '请输入用户名或邮箱', trigger: 'blur'}
                ],
                password: [
                    {required: true, message: '请输入密码', trigger: 'blur'}
                ]
            }
        }
    },
    methods: {
        submitForm(formName) {
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    this.$api.auth.login(this.userForm).then(data => {
                        if (data.success) {
                            this.$router.push('/')
                            this.$util.message.success('登录成功!')
                        } else {
                            this.$util.message.error('登录失败,' + data.msg)
                        }
                    })
                } else {
                    this.$util.message.error('登录失败')
                    return false
                }
            })
        }
    }
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
