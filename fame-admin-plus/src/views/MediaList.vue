<template>
  <div>
    <MediaUpload :after-upload="initMediaData"></MediaUpload>
    <el-divider></el-divider>
    <div class="media-list">
      <el-row>
        <el-col
          v-for="media in mediaList"
          :key="media.id"
          class="block"
          :xs="24"
          :sm="12"
          :md="12"
          :lg="6"
          :xl="4"
        >
          <MediaItem :media-val="media" :after-delete="initMediaData"></MediaItem>
        </el-col>
      </el-row>
    </div>
    <pagination v-model:currentPage="currentPage" v-model:page-size="pageSize" :total="total" />
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive, onMounted, watch } from 'vue'
  import { RestResponse, Page } from '~/types/common'
  import { getServerMediaUrl, handleRestResponse } from '~/utils'
  import { Api } from '~/api'
  import MediaItem from '~/components/MediaItem.vue'
  import MediaUpload from '~/components/MediaUpload.vue'
  import Pagination from '~/components/layouts/Pagination.vue'
  import { MediaInfo } from '~/types/media'

  const currentPage = ref(1)
  const total = ref(0)
  const pageSize = ref(10)

  const mediaList = reactive<Array<MediaInfo>>([])

  async function initMediaData() {
    const resp = (await Api.pageMedia(currentPage.value, pageSize.value)) as RestResponse<
      Page<MediaInfo>
    >
    handleRestResponse(resp, (page) => {
      mediaList.splice(0)
      total.value = page.total
      pageSize.value = page.pageSize
      for (let key in page.list) {
        let mediaItem = page.list[key]
        if (mediaItem.thumbUrl && mediaItem.thumbUrl !== '') {
          mediaItem.showUrl = getServerMediaUrl(mediaItem.thumbUrl)
        } else {
          mediaItem.showUrl = getServerMediaUrl(mediaItem.url)
        }
        mediaList.push(mediaItem)
      }
      console.log(mediaList)
    })
  }

  watch(
    () => currentPage.value,
    () => initMediaData()
  )

  watch(
    () => pageSize.value,
    () => initMediaData()
  )

  onMounted(() => {
    initMediaData()
  })
</script>

<style scoped>
  .el-table {
    border: 1px solid #e6ebf5;
  }

  .media-list .block {
    padding: 12px;
    text-align: center;
    border-left: 1px solid #eff2f6;
    border-right: 1px solid #eff2f6;
    display: inline-block;
    box-sizing: border-box;
    vertical-align: top;
  }
</style>
