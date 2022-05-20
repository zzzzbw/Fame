<template>
  <div class="upload-component">
    <el-button
      type="primary"
      size="large"
      :icon="UploadFilled"
      @click="uploadVisible = !uploadVisible"
    >
      <span v-if="uploadVisible">隐藏</span>
      <span v-else>上传</span>
    </el-button>
    <el-button v-show="uploadVisible" type="info" size="large" @click="clearUploadFile(uploadRef)">
      清空上传列表
    </el-button>
    <el-collapse-transition>
      <el-upload
        v-show="uploadVisible"
        ref="uploadRef"
        accept="image/*"
        class="upload-container"
        drag
        :multiple="true"
        list-type="picture"
        :action="uploadUrl"
        :headers="uploadHeaders"
        :with-credentials="true"
        :data="uploadData"
        :before-upload="beforeUpload"
        :on-success="successUpload"
        :on-error="errorUpload"
      >
        <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
        <div class="el-upload__text"> 将文件拖到此处或<em>点击上传</em> </div>
        <template #tip>
          <div class="el-upload__tip"> 只能上传图片文件，且不超过10m </div>
        </template>
      </el-upload>
    </el-collapse-transition>
  </div>
</template>

<script lang="ts">
  import { defineComponent, ref, reactive } from 'vue'
  import { RestResponse } from '~/types'
  import { ElMessage, ElUpload } from 'element-plus'
  import { getServerUploadMediaUrl } from '~/utils'
  import { UploadFilled } from '@element-plus/icons-vue'
  import dayjs from 'dayjs'

  type ElUploadInstance = InstanceType<typeof ElUpload>

  export default defineComponent({
    components: { UploadFilled },
    props: {
      afterUpload: {
        type: Function,
        default: null
      }
    },
    setup(props) {
      const uploadRef = ref<ElUploadInstance>()

      const uploadVisible = ref(false)
      const uploadUrl = ref(getServerUploadMediaUrl())
      const uploadHeaders = reactive({ Authorization: 'Bearer ' + localStorage.token })
      const uploadData = reactive({ path: '' })

      function clearUploadFile(uploadRef: InstanceType<typeof ElUpload>) {
        uploadRef.clearFiles()
      }

      function beforeUpload(file: File) {
        let fileName = file.name

        const size = file.size / (1024 * 1024)
        if (size > 10) {
          ElMessage.error(fileName + '大于10m')
          return false
        }

        uploadData.path = dayjs(new Date()).format('YYYY/MM')
      }

      function successUpload(response: RestResponse<void>, file: File) {
        if (response.success) {
          ElMessage.success('上传' + file.name + '成功!')
        } else {
          ElMessage.error('上传' + file.name + '失败!' + response.msg)
        }
        if (props.afterUpload) {
          props.afterUpload(response, file)
        }
      }
      function errorUpload(err: Error, file: File) {
        ElMessage.error('网络异常,上传' + file.name + '失败!')
        console.log(err)
      }

      return {
        UploadFilled,
        uploadRef,
        uploadVisible,
        uploadUrl,
        uploadHeaders,
        uploadData,
        clearUploadFile,
        beforeUpload,
        successUpload,
        errorUpload
      }
    }
  })
</script>

<style>
  .el-upload--picture {
    display: block;
  }

  .el-upload-dragger {
    width: 100%;
  }
</style>

<style scoped>
  .upload-component {
    margin-bottom: 24px;
  }

  .upload-container {
    margin-top: 24px;
    text-align: center;
  }

  .upload-container .el-icon-upload {
    margin-top: 20px;
  }

  .el-upload__tip {
    text-align: left;
  }
  /* flow */
  .flow-enter-active,
  .flow-leave-active {
    transition: all 0.5s;
  }

  .flow-enter,
  .flow-leave {
    transform: translateY(-20px);
    opacity: 0;
  }
</style>
