<template>
  <el-upload
    ref="importUpload"
    class="inline-upload"
    accept="md"
    action="#"
    :show-file-list="false"
    :auto-upload="false"
    :on-change="onFileChange"
  >
    <el-button size="small" type="danger">导入</el-button>
  </el-upload>
</template>

<script>
export default {
  name: 'ArticleUpload',
  props: {
    articleId: {
      type: Number,
      default: null,
    },
    articleTitle: {
      type: String,
      default: '',
    },
    afterImport: {
      type: Function,
      default: function () {},
    },
  },
  data: function () {
    return {}
  },
  methods: {
    onFileChange(file) {
      let _this = this
      // 提示框内容
      const msgData = []
      const h = this.$createElement
      msgData.push(h('p', null, '文件[' + file.name + ']'))
      msgData.push(h('p', null, '将覆盖'))
      msgData.push(h('p', null, '文章[' + this.articleTitle + ']'))
      msgData.push(h('p', null, '是否继续?'))

      this.$confirm('提示', {
        message: h('div', null, msgData),
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          if (!_this.beforeImport(file)) {
            return
          }

          const articleId = _this.articleId
          _this.$api.auth
            .importArticle(file.raw, articleId)
            .then((data) => {
              _this.successImport(data, file)
            })
            .catch((err) => {
              _this.errorImport(err, file)
            })
        })
        .catch(() => {})
    },
    beforeImport(file) {
      let fileName = file.name

      const size = file.size / (1024 * 1024)
      if (size > 10) {
        this.$util.message.error(fileName + '大于10m')
        return false
      }
      return true
    },
    successImport(response, file) {
      if (response.success) {
        this.$util.message.success('导入' + file.name + '成功!')
      } else {
        this.$util.message.error('导入' + file.name + '失败!' + response.msg)
      }
      this.afterImport()
    },
    errorImport(err, file) {
      this.$util.message.error('导入' + file.name + '失败!')
      console.log(err)
    },
  },
}
</script>

<style scoped>
.inline-upload {
  display: inline;
  margin-right: 10px;
}
</style>
