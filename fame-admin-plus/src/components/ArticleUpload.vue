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
    <el-button ref="importRef" size="small" type="danger">导入</el-button>
  </el-upload>
  <el-dialog v-model="dialogVisible" title="Tips" width="30%">
    <span v-for="msg in uploadConfirmMsg" :key="msg">
      {{ msg }}
    </span>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmEvent">确定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script lang="ts">
  import { defineComponent, ref, reactive } from 'vue'
  import { Api } from '~/api'
  import { RestResponse } from '~/types'
  import { handleRestResponse } from '~/utils'
  import { ElButton, ElMessage } from 'element-plus'

  type ElButtonInstance = InstanceType<typeof ElButton>

  export default defineComponent({
    props: {
      articleId: {
        type: Number,
        default: null
      },
      articleTitle: {
        type: String,
        default: ''
      },
      afterImport: {
        type: Function,
        default: null
      }
    },
    setup(props) {
      const dialogVisible = ref(false)
      const importRef = ref<ElButtonInstance>()
      const uploadConfirmMsg = reactive<Array<string>>([])

      let uploadFile: any

      function onFileChange(file: File) {
        dialogVisible.value = true

        uploadConfirmMsg.push('文件[' + file.name + ']')
        uploadConfirmMsg.push('将覆盖')
        uploadConfirmMsg.push('文章[' + props.articleTitle + ']')
        uploadConfirmMsg.push('是否继续?')
        uploadFile = file
      }

      async function confirmEvent() {
        if (!beforeImport(uploadFile)) {
          return
        }

        const resp = (await Api.importArticle(
          uploadFile.raw,
          props.articleId
        )) as RestResponse<void>
        handleRestResponse(resp, () => {
          ElMessage.success('导入' + uploadFile?.name + '成功!')
          if (props.afterImport) {
            props.afterImport()
          }
        })
        uploadFile = null
      }

      function beforeImport(file: File) {
        if (!file) {
          ElMessage.error('上传文件不存在')
        }

        let fileName = file.name

        const size = file.size / (1024 * 1024)
        if (size > 10) {
          ElMessage.error(fileName + '大于10m')
          return false
        }
        return true
      }

      return {
        uploadConfirmMsg,
        importRef,
        dialogVisible,
        onFileChange,
        confirmEvent
      }
    }
  })
</script>

<style scoped>
  .inline-upload {
    line-height: normal;
    display: inline;
    margin-right: 10px;
  }
</style>
