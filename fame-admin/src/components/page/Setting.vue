<template>
  <div>
    <el-tabs type="border-card">
      <el-tab-pane label="网站设置">
        <el-form ref="form" v-model="options">
          <el-form-item label="博客名:">
            <el-input v-model="options.blog_name" placeholder="请输入博客名" />
          </el-form-item>

          <el-form-item label="博客网址:">
            <el-input
              v-model="options.blog_website"
              placeholder="请输入博客网址"
            />
          </el-form-item>

          <el-form-item label="博客底部信息:">
            <el-input
              type="textarea"
              :rows="6"
              v-model="options.blog_footer"
              placeholder="请输入博客底部信息,可以使用html语句"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="info" size="small" @click="submitOptions"
              >保存修改
            </el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="SEO 设置">
        <el-form ref="form" v-model="options">
          <el-form-item label="网站名称(Title):">
            <el-input
              v-model="options.meta_title"
              placeholder="请输入网站名称"
            />
          </el-form-item>
          <el-form-item label="网站描述(description):">
            <el-input
              v-model="options.meta_description"
              placeholder="请输入网站描述"
            />
          </el-form-item>
          <el-form-item label="网站关键字(keywords):">
            <el-input
              v-model="options.meta_keywords"
              placeholder="请输入网站关键字"
            />
          </el-form-item>

          <el-form-item label="Google站点验证(google-site-verification):">
            <el-input
              v-model="options.google_site_verification"
              placeholder="请输入Google站点验证"
            />
          </el-form-item>

          <el-form-item label="Baidu站点验证(baidu-site-verification):">
            <el-input
              v-model="options.baidu_site_verification"
              placeholder="请输入Baidu站点验证"
            />
          </el-form-item>

          <el-form-item>
            <el-button type="info" size="small" @click="submitOptions"
              >保存修改
            </el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="邮箱设置">
        <el-form ref="form" v-model="options">
          <el-form-item label="是否设置邮箱提醒:">
            <el-switch v-model="options.is_email"></el-switch>
          </el-form-item>
          <div v-show="options.is_email">
            <el-form-item label="邮箱:">
              <el-input
                v-model="options.email_username"
                placeholder="请输入邮箱号"
              />
            </el-form-item>
            <el-form-item label="邮箱密码:">
              <el-input
                v-model="options.email_password"
                placeholder="请输入邮箱密码"
              />
            </el-form-item>
            <el-form-item label="主机名:">
              <el-input
                v-model="options.email_host"
                placeholder="请输入主机名，如smtp.163.com"
              />
            </el-form-item>
            <el-form-item label="端口号:">
              <el-input
                v-model="options.email_port"
                placeholder="请输入端口号"
              />
            </el-form-item>

            <el-form-item>
              <el-button type="info" size="small" @click="submitOptions"
                >保存修改
              </el-button>
            </el-form-item>
          </div>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="用户信息">
        <el-form ref="form" v-model="userForm">
          <p class="tip">修改后需重新登录</p>
          <el-form-item label="用户名:">
            <el-input v-model="userForm.username" placeholder="请输入用户名" />
          </el-form-item>
          <el-form-item label="邮箱:">
            <el-input v-model="userForm.email" placeholder="请输入邮箱" />
          </el-form-item>
          <el-form-item>
            <el-button type="info" size="small" @click="submitUser"
              >保存修改
            </el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="个人设置">
        <el-form ref="form" v-model="passwordForm">
          <p class="tip">修改后需重新登录</p>
          <el-form-item label="原密码:">
            <el-input
              type="password"
              v-model="passwordForm.oldPassword"
              placeholder="请输入原密码"
            />
          </el-form-item>
          <el-form-item label="新密码:">
            <el-input
              type="password"
              v-model="passwordForm.newPassword"
              placeholder="请输入新密码"
            />
          </el-form-item>
          <el-form-item label="确认新密码:">
            <el-input
              type="password"
              v-model="passwordForm.repeatPassword"
              placeholder="请输入确认新密码"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="info" size="small" @click="submitPassword"
              >保存修改
            </el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script type="text/ecmascript-6">
export default {
    data: function () {
        return {
            userForm: {
                username: '',
                email: ""
            },
            passwordForm: {
                oldPassword: '',
                newPassword: '',
                repeatPassword: ''
            },
            options: {}
        }
    },
    methods: {
        getUser () {
            this.$api.auth.getUser().then(data => {
                this.userForm.username = data.data.username
                this.userForm.email = data.data.email
            })
        },
        getOptions () {
            this.$api.auth.getOptions().then(data => {
                this.options = data.data
                this.options.is_email = this.options.is_email && this.options.is_email === 'true';
            })
        },
        submitUser () {
            this.$api.auth.resetUser(this.userForm.username, this.userForm.email).then(data => {
                if (data.data === true) {
                    this.$message({
                        message: '更新设置成功!',
                        type: 'success'
                    })
                } else {
                    const message = data.msg || '保存失败,未更新数据库'
                    this.$message({
                        message: message,
                        type: 'error'
                    })
                }
                this.$router.push('/admin/login')
            })
        },
        submitPassword () {
            if (this.passwordForm.newPassword !== this.passwordForm.repeatPassword) {
                this.$message({
                    message: '两次输入的密码不同',
                    type: 'error'
                })
                return
            }

            this.$api.auth.resetPassword(this.passwordForm.oldPassword, this.passwordForm.newPassword).then(data => {
                if (data.data === true) {
                    this.$message({
                        message: '更新设置成功!',
                        type: 'success'
                    })
                } else {
                    const message = data.msg || '保存失败,未更新数据库'
                    this.$message({
                        message: message,
                        type: 'error'
                    })
                }
                this.$router.push('/admin/login')
            })
        },
        submitOptions () {
            this.$api.auth.saveOptions(this.options)
                .then(() => {
                    this.$message({
                        message: '保存成功!',
                        type: 'success'
                    })
                })
        }
    },
    mounted () {
        this.getUser()
        this.getOptions()
    }
}
</script>

<style scoped>
.tip {
  font-size: 14px;
  color: #5e6d82;
  line-height: 1.5em;
}
</style>
