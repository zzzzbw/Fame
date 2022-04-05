<template>
  <div>
    <el-tabs>
      <el-tab-pane label="网站设置">
        <el-form ref="websiteFormRef" :model="options" :rules="websiteRules" label-position="top">
          <el-form-item label="博客名:">
            <el-input v-model="options.blog_name" placeholder="请输入博客名" />
          </el-form-item>
          <el-form-item label="博客网址:" prop="blog_website">
            <el-input
              v-model="options.blog_website"
              placeholder="请输入博客网址（http, https:// 开头）"
            />
          </el-form-item>
          <el-form-item label="博客底部信息:">
            <el-input
              v-model="options.blog_footer"
              type="textarea"
              :rows="6"
              placeholder="请输入博客底部信息,可以使用html语句"
            />
          </el-form-item>
          <el-form-item label="文章预览分隔符:" prop="summary_flag">
            <el-input
              v-model="options.summary_flag"
              placeholder="文章预览分隔符,用于分割文章的预览部分，如不输入则预览255个字符"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="small" @click="submitForm(websiteFormRef)"
              >保存修改
            </el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="SEO 设置">
        <el-form ref="seoFormRef" :model="options" label-position="top">
          <el-form-item label="网站名称(Title):">
            <el-input v-model="options.meta_title" placeholder="请输入网站名称" />
          </el-form-item>
          <el-form-item label="网站描述(description):">
            <el-input v-model="options.meta_description" placeholder="请输入网站描述" />
          </el-form-item>
          <el-form-item label="网站关键字(keywords):">
            <el-input v-model="options.meta_keywords" placeholder="请输入网站关键字" />
          </el-form-item>

          <el-form-item label="Google站点验证(google-site-verification):">
            <el-input
              v-model="options.google_site_verification"
              placeholder="请输入Google站点验证"
            />
          </el-form-item>

          <el-form-item label="Baidu站点验证(baidu-site-verification):">
            <el-input v-model="options.baidu_site_verification" placeholder="请输入Baidu站点验证" />
          </el-form-item>

          <el-form-item label="Google站点分析(google_analytics):">
            <el-input v-model="options.google_analytics" placeholder="UA-XXXXXXXX-X" />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" size="small" @click="submitForm(seoFormRef)"
              >保存修改</el-button
            >
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="邮箱设置">
        <el-form ref="emailFormRef" :model="options" :rules="emailRules" label-position="top">
          <el-form-item label="是否设置邮箱提醒:">
            <el-switch v-model="options.is_email"></el-switch>
          </el-form-item>
          <div v-show="options.is_email">
            <el-form-item label="邮箱:" prop="email_username">
              <el-input v-model="options.email_username" placeholder="请输入邮箱号" />
            </el-form-item>
            <el-form-item label="邮箱密码:" prop="email_password">
              <el-input v-model="options.email_password" placeholder="请输入邮箱密码" />
            </el-form-item>
            <el-form-item label="主机名:" prop="email_host">
              <el-input v-model="options.email_host" placeholder="请输入主机名，如smtp.163.com" />
            </el-form-item>
            <el-form-item label="端口号:" prop="email_port">
              <el-input v-model="options.email_port" placeholder="请输入端口号" />
            </el-form-item>
            <el-form-item label="邮件标题:" prop="email_subject">
              <el-input v-model="options.email_subject" placeholder="请输入邮件标题" />
            </el-form-item>
          </div>
          <el-form-item>
            <el-button type="primary" size="small" @click="submitForm(emailFormRef)"
              >保存修改
            </el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="用户信息">
        <el-form ref="userFormRef" :model="userForm" :rules="userRules" label-position="top">
          <p class="tip">修改后需重新登录</p>
          <el-form-item label="用户名:" prop="username">
            <el-input v-model="userForm.username" placeholder="请输入用户名" />
          </el-form-item>
          <el-form-item label="邮箱:" prop="email">
            <el-input v-model="userForm.email" placeholder="请输入邮箱" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="small" @click="submitForm(userFormRef)"
              >保存修改
            </el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="个人设置">
        <el-form
          ref="passwordFormRef"
          :model="passwordForm"
          :rules="passwordRules"
          label-position="top"
        >
          <p class="tip">修改后需重新登录</p>
          <el-form-item label="原密码:" prop="oldPassword">
            <el-input
              v-model="passwordForm.oldPassword"
              type="password"
              placeholder="请输入原密码"
            />
          </el-form-item>
          <el-form-item label="新密码:" prop="newPassword">
            <el-input
              v-model="passwordForm.newPassword"
              type="password"
              placeholder="请输入新密码"
            />
          </el-form-item>
          <el-form-item label="确认新密码:" prop="repeatPassword" required>
            <el-input
              v-model="passwordForm.repeatPassword"
              type="password"
              placeholder="请输入确认新密码"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="small" @click="submitForm(passwordFormRef)"
              >保存修改
            </el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script lang="ts">
  import { defineComponent, ref, reactive, onMounted } from 'vue'
  import router from '~/router'
  import type { ElForm } from 'element-plus'
  import { ElMessage } from 'element-plus'
  import { RestResponse } from '~/types'
  import { handleRestResponse, removeToken } from '~/utils'
  import { Api } from '~/api'

  class User {
    username = ''
    email = ''
  }

  class Option {
    blog_name = ''
    blog_website = ''
    blog_footer = ''
    summary_flag = ''
    meta_title = ''
    meta_description = ''
    meta_keywords = ''
    google_site_verification = ''
    baidu_site_verification = ''
    google_analytics = ''
    is_email = false
    email_username = ''
    email_password = ''
    email_host = ''
    email_port = ''
    email_subject = ''
  }

  type ElFormInstance = InstanceType<typeof ElForm>

  export default defineComponent({
    setup() {
      const websiteFormRef = ref<ElFormInstance>()
      const seoFormRef = ref<ElFormInstance>()
      const emailFormRef = ref<ElFormInstance>()
      const userFormRef = ref<ElFormInstance>()
      const passwordFormRef = ref<ElFormInstance>()

      const userForm = reactive(new User())

      const passwordForm = reactive({
        oldPassword: '',
        newPassword: '',
        repeatPassword: ''
      })

      const optionForm = reactive(new Option())

      const websiteRules = reactive({
        blog_website: [{ type: 'url', message: '请输入正确格式的网址', trigger: 'blur' }]
      })

      const repeatPasswordValidate = (rule: any, value: any, callback: any) => {
        if (value === '') {
          callback(new Error('请再次输入确认密码'))
        } else if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入的密码不一样'))
        } else {
          callback()
        }
      }

      const emailSettingValidate = (rule: any, value: any, callback: any) => {
        if (!optionForm.is_email) {
          return callback()
        }
        if (value === '') {
          callback(new Error('请输入对应信息'))
        }
      }

      const emailRules = reactive({
        email_username: [
          { type: 'email', message: '请输入正确格式的邮箱', trigger: 'blur' },
          { validator: emailSettingValidate, trigger: 'blur' }
        ],
        email_password: [{ validator: emailSettingValidate, trigger: 'blur' }],
        email_host: [{ validator: emailSettingValidate, trigger: 'blur' }],
        email_port: [{ validator: emailSettingValidate, trigger: 'blur' }]
      })

      const userRules = reactive({
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        email: [
          { type: 'email', message: '请输入正确格式的邮箱', trigger: 'blur' },
          { required: true, message: '请输入邮箱', trigger: 'blur' }
        ]
      })

      const passwordRules = reactive({
        oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
        newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }],
        repeatPassword: [{ validator: repeatPasswordValidate, trigger: 'blur' }]
      })

      async function submitUser() {
        const resp = (await Api.resetUser(userForm.username, userForm.email)) as RestResponse<void>
        handleRestResponse(resp, () => {
          ElMessage.success('更新设置成功，请重新登录!')
          removeToken()
          router.push('/login')
        })
      }

      async function submitPassword() {
        const resp = (await Api.resetPassword(
          passwordForm.oldPassword,
          passwordForm.newPassword
        )) as RestResponse<void>
        handleRestResponse(resp, () => {
          ElMessage.success('更新设置成功，请重新登录!')
          removeToken()
          router.push('/login')
        })
      }

      async function submitOption() {
        const resp = (await Api.saveOptions(optionForm)) as RestResponse<void>
        handleRestResponse(resp, () => {
          ElMessage.success('更新设置成功!')
        })
      }

      const submitForm = async (formRef: InstanceType<typeof ElForm>) => {
        if (!formRef) {
          return
        }

        await formRef.validate((valid) => {
          if (!valid) {
            return
          }
          if (formRef === passwordFormRef.value) {
            submitPassword()
          } else if (formRef === userFormRef.value) {
            submitUser()
          } else {
            submitOption()
          }
        })
      }

      async function getUserInfo() {
        const userResp = (await Api.getUser()) as RestResponse<User>
        handleRestResponse(userResp, (user) => {
          userForm.username = user.username
          userForm.email = user.email
        })
      }

      async function getOptions() {
        const optionsResp = (await Api.getOptions()) as RestResponse<Option>
        handleRestResponse(optionsResp, (option) => {
          Object.assign(optionForm, option)
        })
      }

      onMounted(() => {
        getUserInfo()
        getOptions()
      })

      return {
        websiteFormRef,
        seoFormRef,
        emailFormRef,
        userFormRef,
        passwordFormRef,
        userForm,
        passwordForm,
        options: optionForm,
        websiteRules,
        emailRules,
        userRules,
        passwordRules,
        submitForm
      }
    }
  })
</script>

<style scoped>
  .tip {
    text-align: left;
    font-size: 14px;
    color: #5e6d82;
    line-height: 1.5em;
  }
</style>
