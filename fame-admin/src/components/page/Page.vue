<template>
  <div>
    <el-form :rules="rules" ref="pageForm" :model="page">
      <el-row :gutter="30">
        <el-col :xs="24" :sm="16" :md="16" :lg="16">
          <el-form-item prop="title">
            <el-input
              v-model="page.title"
              placeholder="请输入自定义页面标题"
            ></el-input>
          </el-form-item>
          <el-form-item prop="content">
            <mavon-editor
              :toolbars="markdownOption.toolbars"
              :codeStyle="markdownOption.codeStyle"
              v-model="page.content"
            />
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
                  inactive-text="隐藏"
                >
                </el-switch>
              </el-form-item>
              <el-form-item>
                <el-button-group>
                  <el-button type="primary" size="small" @click="onPublish"
                    >发布页面
                  </el-button>
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
import { mavonEditor } from "mavon-editor";
import "mavon-editor/dist/css/index.css";

export default {
  components: {
    mavonEditor
  },
  data: function () {
    return {
      markdownOption: {
        codeStyle: "tomorrow",
        toolbars: {
          bold: true, // 粗体
          italic: true, // 斜体
          header: true, // 标题
          underline: true, // 下划线
          strikethrough: true, // 中划线
          mark: true, // 标记
          superscript: true, // 上角标
          subscript: true, // 下角标
          quote: true, // 引用
          ol: true, // 有序列表
          ul: true, // 无序列表
          link: true, // 链接
          imagelink: true, // 图片链接
          code: true, // code
          table: true, // 表格
          fullscreen: true, // 全屏编辑
          readmodel: true, // 沉浸式阅读
          htmlcode: true, // 展示html源码
          help: true, // 帮助
          /* 1.3.5 */
          undo: true, // 上一步
          redo: true, // 下一步
          trash: true, // 清空
          /* 2.2.1 */
          subfield: true, // 单双栏模式
          preview: true // 预览
        }
      },
      page: {
        id: '',
        title: '',
        content: '',
        status: ''
      },
      rules: {
        title: [
          { required: true, message: '文章标题必须输入', trigger: 'blur' }
        ],
        content: [
          { required: true, message: '文章内容不能为空', trigger: 'blur' }
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
@import "~simplemde/dist/simplemde.min.css";
@import "~highlight.js/styles/googlecode.css";

.markdown-editor .CodeMirror {
  height: calc(90vh - 200px);
}
</style>

<style scoped></style>
