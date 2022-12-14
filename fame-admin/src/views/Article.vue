<template>
  <div>
    <el-form ref="articleFormRef" label-position="top" :rules="rules" :model="article">
      <el-row :gutter="30">
        <el-col :xs="24" :sm="16" :md="19" :lg="19">
          <el-form-item prop="title">
            <el-input v-model="article.title" placeholder="请输入文章标题"></el-input>
          </el-form-item>
          <el-form-item prop="content">
            <BytemdEditor v-model:contentVal="article.content" class="md-editor" />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="8" :md="5" :lg="5">
          <div class="panel">
            <div class="panel-content">
              <el-form-item label="标签">
                <el-select
                  v-model="article.tagIds"
                  class="panel-content-item"
                  multiple
                  filterable
                  placeholder="请选择文章标签"
                >
                  <el-option v-for="tag in tags" :key="tag.id" :label="tag.name" :value="tag.id" />
                </el-select>
              </el-form-item>
              <el-form-item label="分类">
                <el-select
                  v-model="article.categoryId"
                  class="panel-content-item"
                  filterable
                  clearable
                  placeholder="请选择文章分类"
                >
                  <el-option
                    v-for="category in categories"
                    :key="category.id"
                    :label="category.name"
                    :value="category.id"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-checkbox v-model="article.listShow">列表显示</el-checkbox>
                <el-checkbox v-model="article.headerShow">顶栏显示</el-checkbox>
              </el-form-item>
              <el-form-item>
                <el-switch
                  v-model="article.status"
                  :active-value="ArticleStatusEnum.PUBLISH"
                  :inactive-value="ArticleStatusEnum.DRAFT"
                  :active-text="getConstValue(ArticleStatusEnum.PUBLISH, ArticleStatus)"
                  :inactive-text="getConstValue(ArticleStatusEnum.DRAFT, ArticleStatus)"
                >
                </el-switch>
              </el-form-item>
              <el-form-item>
                <el-switch
                  v-model="article.priority"
                  :active-value="ArticlePriorityEnum.TOP"
                  :inactive-value="ArticlePriorityEnum.NORMAL"
                  active-color="#ffd740"
                  :active-text="getConstValue(ArticlePriorityEnum.TOP, ArticlePriority)"
                  :inactive-text="getConstValue(ArticlePriorityEnum.NORMAL, ArticlePriority)"
                >
                </el-switch>
              </el-form-item>
              <el-form-item>
                <el-switch
                  v-model="article.allowComment"
                  active-color="#13ce66"
                  active-text="开启评论"
                  inactive-text="关闭"
                >
                </el-switch>
              </el-form-item>
              <el-form-item label="发布日期">
                <el-date-picker
                  v-model="article.publishTime"
                  class="panel-content-item"
                  type="datetime"
                  placeholder="发布日期"
                  size="small"
                  :editable="flagFalse"
                >
                </el-date-picker>
              </el-form-item>
              <el-form-item>
                <el-button size="small" @click="showMediaDialog()">媒体库</el-button>
              </el-form-item>
              <el-form-item>
                <el-button-group>
                  <el-row>
                    <el-button type="success" size="small" @click="onSave(articleFormRef)"
                      >保存</el-button
                    >
                    <el-button type="primary" size="small" @click="onPublish(articleFormRef)"
                      >发布</el-button
                    >
                    <el-button v-if="article.id" type="info" size="small">
                      <a
                        :href="getFrontArticleUrl(article.id)"
                        target="_blank"
                        style="color: #ffffff"
                        >查看</a
                      >
                    </el-button>
                  </el-row>
                </el-button-group>
              </el-form-item>
              <el-form-item>
                <el-button-group>
                  <el-row>
                    <article-upload
                      :article-id="article.id"
                      :article-title="article.title"
                      :after-import="initArticle"
                    ></article-upload>
                    <el-button size="small" @click="exportArticle">导出</el-button>
                  </el-row>
                </el-button-group>
              </el-form-item>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-form>

    <el-drawer v-model="mediaDialog" direction="rtl">
      <template #title>
        <h4>媒体库</h4>
      </template>
      <template #default>
        <MediaUpload :after-upload="initMedia"></MediaUpload>
        <el-divider></el-divider>
        <div class="media-list">
          <el-row>
            <el-col
              v-for="media in mediaData.data"
              :key="media.id"
              class="block"
              :xs="24"
              :sm="24"
              :md="24"
              :lg="24"
              :xl="12"
            >
              <MediaItem :media-val="media" :after-delete="initMedia"></MediaItem>
            </el-col>
          </el-row>
        </div>
        <div class="admin-page">
          <pagination
            v-model:currentPage="mediaData.currentPage"
            v-model:page-size="mediaData.pageSize"
            :total="mediaData.total"
          />
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive, onMounted, watch } from 'vue'
  import { Api } from '~/api'
  import {
    Page,
    RestResponse,
    ArticleStatusEnum,
    ArticleStatus,
    ArticlePriorityEnum,
    ArticlePriority
  } from '~/types/common'
  import {
    getFrontArticleUrl,
    getServerMediaUrl,
    handleRestResponse,
    getConstValue,
    downloadFile
  } from '~/utils'
  import router from '~/router'
  import BytemdEditor from '~/components/BytemdEditor.vue'
  import { FormInstance, FormRules, ElMessage } from 'element-plus'
  import { AxiosResponse } from 'axios'
  import Pagination from '~/components/layouts/Pagination.vue'
  import MediaItem from '~/components/MediaItem.vue'
  import { Article, ArticleResp } from '~/types/article'
  import { Meta } from '~/types/meta'
  import { MediaInfo } from '~/types/media'

  interface MediaData {
    currentPage: number
    pageSize: number
    total: number
    data: Array<MediaInfo>
  }

  const articleFormRef = ref<FormInstance>()

  const mediaDialog = ref(false)
  const submitting = ref(false)

  const article = reactive<Article>({
    id: undefined,
    title: '',
    tagIds: [],
    categoryId: undefined,
    content: '',
    status: 'PUBLISH',
    listShow: true,
    headerShow: false,
    priority: 0,
    allowComment: true,
    publishTime: new Date().getTime()
  })
  const rules = reactive<FormRules>({
    title: [{ required: true, message: '文章标题必须输入', trigger: 'blur' }],
    content: [{ required: true, message: '文章内容不能为空', trigger: 'blur' }]
  })

  const tags = reactive<Array<Meta>>([])
  const categories = reactive<Array<Meta>>([])
  const flagFalse = ref(false)
  const mediaData = reactive<MediaData>({ currentPage: 0, data: [], pageSize: 10, total: 0 })

  async function initArticle() {
    const id = router.currentRoute.value.params.id
    if (id) {
      const resp = (await Api.getArticle(Number(id))) as RestResponse<ArticleResp>
      handleRestResponse(resp, (data) => {
        Object.assign(article, data)
        article.categoryId = data.category?.id
        article.tagIds = data.tags?.map((tag) => {
          return tag.id
        })
      })
    }
  }

  async function initTags() {
    const resp = (await Api.getAllTags()) as RestResponse<Array<Meta>>
    handleRestResponse(resp, (data) => {
      tags.splice(0)
      for (let key in data) {
        let tag = data[key]
        tags.push(tag)
      }
    })
  }

  async function initCategories() {
    const resp = (await Api.getAllCategories()) as RestResponse<Array<Meta>>
    handleRestResponse(resp, (data) => {
      categories.splice(0)
      for (let key in data) {
        let category = data[key]
        categories.push(category)
      }
    })
  }

  async function initMedia() {
    const resp = (await Api.pageMedia(mediaData.currentPage, mediaData.pageSize)) as RestResponse<
      Page<MediaInfo>
    >
    handleRestResponse(resp, (page) => {
      mediaData.data.splice(0)
      mediaData.total = page.total
      mediaData.pageSize = page.pageSize
      for (let key in page.list) {
        let mediaItem = page.list[key]
        if (mediaItem.thumbUrl && mediaItem.thumbUrl !== '') {
          mediaItem.showUrl = getServerMediaUrl(mediaItem.thumbUrl)
        } else {
          mediaItem.showUrl = getServerMediaUrl(mediaItem.url)
        }
        mediaData.data.push(mediaItem)
      }
    })
  }

  async function exportArticle() {
    if (!article.id) {
      ElMessage.error('该文章还未保存，不能导出')
      return
    }

    const res = (await Api.exportArticle(article.id)) as AxiosResponse
    downloadFile(res)
  }

  function onPublish(formRef: FormInstance | undefined) {
    submitForm(formRef, () => {
      ElMessage.success('发布文章成功!')
      router.push('/article')
    })
  }

  function onSave(formRef: FormInstance | undefined) {
    submitForm(formRef, (article: ArticleResp) => {
      ElMessage.success('发布文章成功!')
      router.currentRoute.value.params.id = String(article.id)
      initArticle()
    })
  }

  const submitForm = async (formRef: FormInstance | undefined, callback: Function) => {
    if (!formRef) {
      return
    }

    if (submitting.value) {
      ElMessage.warning('请不要提交过快!')
      return
    }

    await formRef.validate(async (valid) => {
      if (!valid) {
        return
      }

      submitting.value = true
      console.log(article)
      const resp = (await Api.saveArticle(article)) as RestResponse<ArticleResp>
      handleRestResponse(
        resp,
        (article) => {
          submitting.value = false
          callback(article)
        },
        () => {
          submitting.value = false
          ElMessage.error('提交文章失败,' + resp.msg)
        }
      )
    })
  }

  async function showMediaDialog() {
    await initMedia()
    mediaDialog.value = true
  }

  onMounted(() => {
    initTags()
    initCategories()
    initArticle()
  })

  watch(
    () => router.currentRoute.value,
    () => initArticle()
  )

  watch(
    () => mediaData.currentPage,
    () => initMedia()
  )
</script>

<style scoped>
  .admin-page {
    margin-top: 30px;
    text-align: center;
  }

  .md-editor {
    width: 100%;
    line-height: normal;
    text-align: left;
  }

  a {
    text-decoration: none;
  }

  .panel-content-item {
    width: 100%;
  }
</style>
