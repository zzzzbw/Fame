<template>
  <el-row :gutter="30">
    <el-col :xs="24" :sm="12" :md="12" :lg="12" style="margin-top: 30px">
      <div class="panel">
        <div class="panel-content">
          <div class="header">
            <div class="title">个人设置</div>
          </div>
          <el-form ref="form" v-model="userForm" :label-position="'left'" label-width="100px">
            <el-form-item label="账号:">
              <el-input v-model="userForm.username" placeholder="账号" :disabled="true"/>
            </el-form-item>
            <el-form-item label="原密码:">
              <el-input type="password" v-model="userForm.oldPassword" placeholder="请输入原密码"/>
            </el-form-item>
            <el-form-item label="新密码:">
              <el-input type="password" v-model="userForm.newPassword" placeholder="请输入新密码"/>
            </el-form-item>
            <el-form-item label="确认新密码:">
              <el-input type="password" v-model="userForm.repeatPassword" placeholder="请输入确认新密码"/>
            </el-form-item>
            <el-form-item>
              <el-button type="info" size="small" style="float: right;" @click="submitUser">保存修改
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </el-col>
    <el-col :xs="24" :sm="12" :md="12" :lg="12" style="margin-top: 30px">
      <div class="panel">
        <div class="panel-content">
          <div class="header">
            <div class="title">系统设置(重启服务器后会还原)</div>
          </div>
          <el-form ref="form" v-model="staticForm" :label-position="'left'" label-width="160px">
            <el-form-item label="网站名称(Title):">
              <el-input v-model="staticForm.title" placeholder="请输入网站名称"/>
            </el-form-item>
            <el-form-item label="网站描述(description):">
              <el-input v-model="staticForm.description" placeholder="请输入网站描述"/>
            </el-form-item>
            <el-form-item label="网站关键字(keywords):">
              <el-input v-model="staticForm.keywords" placeholder="请输入网站关键字"/>
            </el-form-item>
            <el-form-item label="是否设置邮箱提醒:">
              <el-switch
                  v-model="staticForm.emailSend">
              </el-switch>
            </el-form-item>
            <el-form-item label="邮箱:" v-show="staticForm.emailSend">
              <el-input v-model="staticForm.emailUsername" placeholder="请输入邮箱号"/>
            </el-form-item>
            <el-form-item label="邮箱密码:" v-show="staticForm.emailSend">
              <el-input v-model="staticForm.emailPassword" placeholder="请输入邮箱密码"/>
            </el-form-item>
            <el-form-item label="主机名:" v-show="staticForm.emailSend">
              <el-input v-model="staticForm.emailHost" placeholder="请输入主机名，如smtp.163.com"/>
            </el-form-item>
            <el-form-item label="端口号:" v-show="staticForm.emailSend">
              <el-input v-model="staticForm.emailPort" placeholder="请输入端口号"/>
            </el-form-item>
            <el-form-item>
              <el-button type="info" size="small" style="float: right;" @click="submitSiteConfig">保存修改
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </el-col>
  </el-row>

</template>

<script type="text/ecmascript-6">
  export default {
    data: function () {
      return {
        userForm: {
          username: '',
          oldPassword: '',
          newPassword: '',
          repeatPassword: ''
        },
        staticForm: {
          title: '',
          description: '',
          keywords: '',
          emailSend: false,
          emailUsername: '',
          emailPassword: '',
          emailHost: '',
          emailPort: ''
        }
      }
    },
    methods: {
      getUsername () {
        this.$api.auth.getUsername().then(data => {
          this.userForm.username = data.data
        })
      },
      getSiteConfig () {
        this.$api.auth.getSiteConfig().then(data => {
          this.staticForm = data.data
        })
      },
      submitUser () {
        this.$api.auth.resetPassword(this.userForm.username, this.userForm.oldPassword, this.userForm.newPassword).then(data => {
          if (data.data === true) {
            this.$message({
              message: '保存成功,请重新登陆',
              type: 'success'
            })
            this.$api.auth.logout()
            this.$router.push('/admin/login')
          } else {
            this.$message({
              message: '保存失败,未更新数据库',
              type: 'error'
            })
          }
        })
      },
      submitSiteConfig () {
        this.$api.auth.saveSiteConfig(this.staticForm.title, this.staticForm.description, this.staticForm.keywords, this.staticForm.emailSend, this.staticForm.emailUsername, this.staticForm.emailPassword, this.staticForm.emailHost, this.staticForm.emailPort
        ).then(() => {
          this.$message({
            message: '保存成功,请重新登陆',
            type: 'success'
          })
          this.$api.auth.logout()
          this.$router.push('/admin/login')
        })
      }
    },
    mounted () {
      this.getUsername()
      this.getSiteConfig()
    }
  }
</script>

<style scoped>
</style>
