<template>
  <el-card :body-style="{ padding: '0px', textAlign: 'center' }" shadow="always">
    <el-image :src="mediaVal.showUrl" fit="cover" @click="showDetailDialog(mediaVal.id)"></el-image>
    <div @click="showDetailDialog(mediaVal.id)">
      <span class="media-title">{{ mediaVal.name }}</span>
    </div>
    <div style="margin-bottom: 14px">
      <el-button-group>
        <el-button size="small" :icon="DocumentCopy" @click="copyUrl(mediaVal.url)"></el-button>
        <el-button
          type="primary"
          size="small"
          :icon="Document"
          @click="copyMarkdownUrl(mediaVal.name, mediaVal.url)"
        >
        </el-button>
        <el-popconfirm
          title="此操作将永久删除该媒体,是否继续?"
          confirm-button-text="确定"
          cancel-button-text="取消"
          :icon="InfoFilled"
          icon-color="red"
          @confirm="deleteMedia(mediaVal.id)"
        >
          <template #reference>
            <el-button size="small" type="danger" :icon="Delete"></el-button>
          </template>
        </el-popconfirm>
      </el-button-group>
    </div>
  </el-card>
</template>

<script setup lang="ts">
  import { InfoFilled, DocumentCopy, Document, Delete } from '@element-plus/icons-vue'
  import { copyText, getServerMediaUrl, handleRestResponse } from '~/utils'
  import { RestResponse } from '~/types/common'
  import { ElMessage } from 'element-plus'
  import { Api } from '~/api'

  const props = defineProps({
    afterDelete: {
      type: Function,
      default: null
    },
    mediaVal: {
      type: Object,
      default: null
    }
  })

  function copyUrl(url: string) {
    copyText(getServerMediaUrl(url))
    ElMessage.success('复制链接到剪切板成功!')
  }

  function copyMarkdownUrl(name: string, url: string) {
    const realUrl = getServerMediaUrl(url)
    let text = '![' + name + '](' + realUrl + ')'
    copyText(text)
    ElMessage.success('复制Markdown格式链接到剪切板成功!')
  }

  async function deleteMedia(id: number) {
    const resp = (await Api.deleteMedia(id)) as RestResponse<void>
    handleRestResponse(resp, () => {
      ElMessage.success('删除成功!')
      if (props.afterDelete) {
        props.afterDelete()
      }
    })
  }

  function showDetailDialog() {
    const url = getServerMediaUrl(props.mediaVal?.url)
    window.open(url, '_blank')
  }
</script>

<style scoped>
  .media-title {
    color: #8492a6;
    font-size: 14px;
    padding: 14px;
    display: inline-block;
    white-space: nowrap;
    width: 60%;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .el-image {
    width: 100%;
    height: 160px;
  }
</style>
