<template>
  <div>
    <el-form label-position="top" :rules="rules" ref="articleForm" :model="article">
      <el-row :gutter="30">
        <el-col :xs="24" :sm="16" :md="16" :lg="16">
          <el-form-item prop="title">
            <el-input v-model="article.title" placeholder="请输入文章标题"></el-input>
          </el-form-item>
          <el-form-item prop="content">
            <md-editor v-model="article.content"></md-editor>
            <!-- 键修饰符，键别名 -->
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="8" :md="8" :lg="8">
          <div class="panel">
            <div class="panel-content">
              <el-form-item label="标签">
                <el-select
                    v-model="article.tags"
                    multiple
                    filterable
                    placeholder="请选择文章标签">
                  <el-option
                      v-for="tag in tags"
                      :key="tag.value"
                      :label="tag.label"
                      :value="tag.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="分类">
                <el-select v-model="article.category" filterable placeholder="请选择文章分类">
                  <el-option
                      v-for="category in categories"
                      :key="category.value"
                      :label="category.label"
                      :value="category.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="状态">
                <el-switch
                    v-model="article.status"
                    active-value="publish"
                    inactive-value="draft"
                    active-text="公开"
                    inactive-text="隐藏">
                </el-switch>
              </el-form-item>
              <el-form-item>
                <el-button-group>
                  <el-button type="primary" size="small" @click="onPublish">发布文章</el-button>
                </el-button-group>
              </el-form-item>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>
  import MDEditor from '../common/MDEditor'

  export default {
    components: {
      'md-editor': MDEditor
    },
    data: function () {
      return {
        article: {
          id: '',
          title: '',
          tags: '',
          category: '',
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
        },
        tags: [],
        categories: [],
        isFullScreen: false
      }
    },
    methods: {
      getArticle () {
        const id = this.$route.params.id
        // 如果有id则表示编辑文章,获取文章信息
        if (id) {
          this.$api.auth.getArticle(id).then(data => {
            this.initArticle(data.data)
          })
        } else { // 如果没有id则表示新增文章,不用清空文章信息
          this.article.id = ''
          this.article.title = ''
          this.article.tags = this.$util.stringToTags('')
          this.article.category = ''
          this.article.content = ''
          this.article.status = this.$util.STATIC.STATUS_PUBLISH
        }
      },
      initArticle (data) {
        this.article.id = data.id
        this.article.title = data.title
        this.article.tags = this.$util.stringToTags(data.tags)
        this.article.category = data.category
        this.article.content = data.content
        this.article.status = data.status
      },
      getTags () {
        this.$api.auth.getAllTags().then(data => {
          if (data.success) {
            for (let key in data.data) {
              let tag = {
                value: data.data[key].name, label: data.data[key].name
              }
              this.tags.push(tag)
            }
          } else {
            this.$message({
              message: '获取标签列表失败',
              type: 'error'
            })
          }
        })
      },
      getCategories () {
        this.$api.auth.getAllCategories().then(data => {
          if (data.success) {
            for (let key in data.data) {
              let category = {
                value: data.data[key].name, label: data.data[key].name
              }
              this.categories.push(category)
            }
          } else {
            this.$message({
              message: '获取分类列表失败',
              type: 'error'
            })
          }
        })
      },
      saveArticle (formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let params = this.article
            params.tags = this.$util.tagsToString(this.article.tags)
            this.$api.auth.saveArticle(params).then(data => {
              if (data.success) {
                this.$router.push('/admin/article')
                this.$message({
                  message: '发布文章成功!',
                  type: 'success'
                })
              } else {
                this.$message({
                  message: '发布文章失败,' + data.msg,
                  type: 'error'
                })
              }
            })
          }
        })
      },
      onPublish () {
        this.saveArticle('articleForm')
      },
      init () {
        this.getArticle()
        this.getTags()
        this.getCategories()
      }
    },
    mounted () {
      this.init()
    },
    watch: {
      // 监听route刷新绑定的article数据
      $route () {
        this.getArticle()
      }
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
  .el-select {
    width: 100%;
  }
</style>

