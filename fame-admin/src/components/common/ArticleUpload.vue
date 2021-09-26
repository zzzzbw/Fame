<template>
  <div>
    <el-upload
      ref="importUpload"
      class="inline-upload"
      accept="md"
      :show-file-list="false"
      :action="importAction"
      :data="importData"
      :with-credentials="true"
      :before-upload="beforeImport"
      :on-success="successImport"
      :on-error="errorImport"
    >
    </el-upload>
    <el-button size="small" @click="handleImport">导入</el-button>
  </div>
</template>

<script>
import serverConfig from '../../../server-config'

export default {
  name: 'ArticleUpload',
  props: {
    articleId: {
      type: Number,
      default: null,
    },
    afterImport: {
      type: Function,
      default: function () {},
    },
  },
  data: function () {
    return {
      importAction: serverConfig.api + 'api/admin/backup/import',
      importData: {
        articleId: null,
      },
    }
  },
  methods: {
    handleImport() {
      let self = this
      this.$confirm('原有文章内容将被替换, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'danger',
      })
        .then(() => {
          self.$refs['importUpload'].$refs['upload-inner'].handleClick()
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

      this.importData.articleId = this.articleId
    },
    successImport(response, file) {
      if (response.success) {
        this.$util.message.success('上传' + file.name + '成功!')
      } else {
        this.$util.message.error('上传' + file.name + '失败!' + response.msg)
      }
      this.afterImport()
    },
    errorImport(err, file) {
      this.$util.message.error('上传' + file.name + '失败!')
      console.log(err)
    },
  },
}
</script>

<style scoped>
.inline-upload {
  display: inline;
}
</style>
