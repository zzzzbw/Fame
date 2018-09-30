<template>
  <div>
    <el-form :rules="rules" ref="pageForm" :model="page">
      <el-row :gutter="30">
        <el-col :xs="24" :sm="16" :md="16" :lg="16">
          <el-form-item prop="title">
            <el-input v-model="page.title" placeholder="请输入自定义页面标题"></el-input>
          </el-form-item>
          <el-form-item prop="content">
            <md-editor v-model="page.content"></md-editor>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="8" :md="8" :lg="8">
          <div class="panel">
            <div class="panel-content">
              <el-form-item label="状态">
                <el-switch
                    v-model="page.status"
                    active-value="publish"
                    inactive-value="draft"
                    active-text="公开"
                    inactive-text="隐藏">
                </el-switch>
              </el-form-item>
              <el-form-item>
                <el-button-group>
                  <el-button type="primary" size="small" @click="onPublish">发布页面</el-button>
                </el-button-group>
              </el-form-item>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script type="text/ecmascript-6">
  import MDEditor from '../common/MDEditor'

  export default {
    components: {
      'md-editor': MDEditor
    },
    data: function () {
      return {
        page: {
          id: '',
          title: '',
          content: '',
          status: ''
        },
        rules: {
          title: [
            {required: true, message: '文章标题必须输入', trigger: 'blur'}
          ],
          content: [
            {required: true, message: '文章内容不能为空', trigger: 'blur'}
          ]
        }
      }
    },
    methods: {
      getPage () {
        const id = this.$route.params.id
        if (id) {
          this.$api.auth.getPage(id).then(data => {
            this.page.id = data.data.id
            this.page.title = data.data.title
            this.page.content = data.data.content
            this.page.status = data.data.status
          })
        } else {
          this.page.id = ''
          this.page.title = ''
          this.page.content = ''
          this.page.status = this.$util.STATIC.STATUS_PUBLISH
        }
      },
      savePage (formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.$api.auth.savePage(this.page).then(() => {
              this.$router.push('/admin/page')
              this.$message({
                message: '发布自定义页面成功!',
                type: 'success'
              })
            })
          }
        })
      },
      onPublish () {
        this.savePage('pageForm')
      }
    },
    mounted () {
      this.getPage()
    }
  }
</script>

<style>
  @import '~simplemde/dist/simplemde.min.css';
  @import "~highlight.js/styles/googlecode.css";

  .markdown-editor .CodeMirror {
    height: calc(90vh - 200px);
  }
</style>

<style scoped>
</style>

