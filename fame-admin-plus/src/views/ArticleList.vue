<template>
  <div>
    <div class="tool-container">
      <el-row>
        <el-col :xs="24" :sm="24" :md="16" :lg="18">
          <el-row>
            <el-col :xs="24" :sm="24" :md="12" :lg="5">
              <div class="tool-container-item">
                <span>状态：</span>
                <el-radio-group v-model="tool.status" size="small" @change="initArticleData">
                  <el-radio-button label="">全部</el-radio-button>
                  <el-radio-button v-for="item in ArticleStatus" :key="item.key" :label="item.key">
                    {{ item.value }}
                  </el-radio-button>
                </el-radio-group>
              </div>
            </el-col>
            <el-col :xs="24" :sm="24" :md="12" :lg="5">
              <div class="tool-container-item">
                <span>类型：</span>
                <el-radio-group v-model="tool.priority" size="small" @change="initArticleData">
                  <el-radio-button label="">全部</el-radio-button>
                  <el-radio-button
                    v-for="item in ArticlePriority"
                    :key="item.key"
                    :label="item.key"
                  >
                    {{ item.value }}
                  </el-radio-button>
                </el-radio-group>
              </div>
            </el-col>
            <el-col :xs="24" :sm="24" :md="12" :lg="5">
              <div class="tool-container-item">
                <span>列表显示：</span>
                <el-radio-group v-model="tool.listShow" size="small" @change="initArticleData">
                  <el-radio-button :label="undefined">全部</el-radio-button>
                  <el-radio-button :label="true">是</el-radio-button>
                  <el-radio-button :label="false">否</el-radio-button>
                </el-radio-group>
              </div>
            </el-col>
            <el-col :xs="24" :sm="24" :md="12" :lg="5">
              <div class="tool-container-item">
                <span>顶部显示：</span>
                <el-radio-group v-model="tool.headerShow" size="small" @change="initArticleData">
                  <el-radio-button :label="undefined">全部</el-radio-button>
                  <el-radio-button :label="true">是</el-radio-button>
                  <el-radio-button :label="false">否</el-radio-button>
                </el-radio-group>
              </div>
            </el-col>
          </el-row>
        </el-col>
        <el-col :xs="24" :sm="24" :md="8" :lg="6">
          <el-row :gutter="20">
            <el-col :xs="24" :sm="24" :md="12" :lg="16">
              <div class="tool-container-item">
                <el-input
                  v-model="tool.title"
                  placeholder="搜索文章标题"
                  size="small"
                  prefix-icon="el-icon-search"
                  clearable
                  style="max-width: 350px"
                  @change="initArticleData"
                ></el-input>
              </div>
            </el-col>
            <el-col :xs="24" :sm="24" :md="12" :lg="8">
              <div class="tool-container-item">
                <el-button type="info" size="small" :icon="Edit" @click="handleNew"
                  >新建文章
                </el-button>
              </div>
            </el-col>
          </el-row>
        </el-col>
      </el-row>
    </div>

    <el-table :data="articleList" border stripe style="width: 100%">
      <el-table-column prop="id" label="id" width="60"></el-table-column>
      <el-table-column prop="title" label="标题" show-overflow-tooltip>
        <template #default="scope">
          <el-link :href="scope.row.frontUrl" target="_blank" type="primary"
            >{{ scope.row.title }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column prop="category" label="分类" show-overflow-tooltip>
        <template #default="scope">
          <span :class="{ meta: scope.row.category }">{{ scope.row.category?.name }}</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="listShow"
        label="列表显示"
        width="80"
        show-overflow-tooltip
        align="center"
      >
        <template #default="scope">
          <el-icon v-if="scope.row.listShow" size="24" color="#409eff">
            <Check />
          </el-icon>
          <el-icon v-else size="24">
            <Minus />
          </el-icon>
        </template>
      </el-table-column>
      <el-table-column
        prop="headerShow"
        label="顶部显示"
        width="80"
        show-overflow-tooltip
        align="center"
      >
        <template #default="scope">
          <el-icon v-if="scope.row.headerShow" size="24" color="#409eff">
            <Check />
          </el-icon>
          <el-icon v-else size="24">
            <Minus />
          </el-icon>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100" show-overflow-tooltip align="center">
        <template #default="scope">
          <el-tag
            :type="scope.row.status === '公开' ? 'success' : 'warning'"
            effect="dark"
            disable-transitions
            >{{ scope.row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="priority"
        label="类型"
        width="100"
        show-overflow-tooltip
        align="center"
      >
        <template #default="scope">
          <el-tag
            :type="scope.row.priority === '置顶' ? 'warning' : ''"
            effect="dark"
            disable-transitions
            >{{ scope.row.priority }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="发布日期" width="160" show-overflow-tooltip>
        <template #default="scope">
          <i class="el-icon-time"></i>
          <span style="margin-left: 10px">{{ scope.row.publishTime }}</span>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" width="150">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row.id)">编辑</el-button>
          <el-popconfirm
            title="此操作将永久删除该文章,是否继续?"
            confirm-button-text="确定"
            cancel-button-text="取消"
            :icon="InfoFilled"
            icon-color="red"
            @confirm="deleteArticle(scope.row.id)"
          >
            <template #reference>
              <el-button size="small" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-model:currentPage="currentPage" v-model:page-size="pageSize" :total="total" />
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive, onMounted, watch } from 'vue'
  import { Edit, InfoFilled, Check, Minus } from '@element-plus/icons-vue'
  import { Api } from '~/api'
  import { RestResponse, Page } from '~/types'
  import { getConstValue, getFrontArticleUrl, handleRestResponse } from '~/utils'
  import { ArticleStatus, ArticlePriority } from '~/types'
  import router from '~/router'
  import { ElMessage } from 'element-plus'
  import dayjs from 'dayjs'
  import Pagination from '~/components/layouts/Pagination.vue'

  interface ArticleListItem {
    id: number
    title: string
    frontUrl: string
    publishTime: string
    category: { name: string }
    listShow: string
    headerShow: string
    status: string
    priority: string
  }

  const currentPage = ref(1)
  const total = ref(0)
  const pageSize = ref(10)

  const tool = reactive({
    status: '',
    title: '',
    priority: '',
    listShow: undefined,
    headerShow: undefined
  })
  const articleList = reactive<Array<ArticleListItem>>([])

  function handleNew() {
    router.push('/article/edit')
  }

  function handleEdit(id: number) {
    router.push('/article/edit/' + id)
  }

  async function deleteArticle(id: number) {
    const resp = (await Api.deleteArticle(id)) as RestResponse<void>
    handleRestResponse(resp, () => {
      ElMessage.success('删除成功!')
      initArticleData()
    })
  }

  async function initArticleData() {
    const resp = (await Api.pageArticle(currentPage.value, pageSize.value, tool)) as RestResponse<
      Page<ArticleListItem>
    >

    handleRestResponse(resp, (page) => {
      total.value = page.total
      pageSize.value = page.pageSize
      articleList.splice(0)
      for (let key in page.list) {
        let article = page.list[key]

        article.frontUrl = getFrontArticleUrl(article.id)
        article.publishTime = dayjs(article.publishTime).format('YYYY-MM-DD HH:mm')
        article.status = getConstValue(article.status, ArticleStatus)
        article.priority = getConstValue(article.priority, ArticlePriority)
        articleList.push(article)
      }
    })
  }

  watch(
    () => currentPage.value,
    () => initArticleData()
  )

  watch(
    () => pageSize.value,
    () => initArticleData()
  )

  onMounted(() => {
    initArticleData()
  })
</script>

<style>
  .el-checkbox__input.is-disabled.is-checked .el-checkbox__inner {
    color: #ffffff !important;
    background-color: #1890ff !important;
    border-color: #1890ff !important;
    -webkit-box-shadow: -1px 0 0 0 #74bcff !important;
    box-shadow: -1px 0 0 0 #74bcff !important;
  }

  .el-checkbox__input.is-disabled.is-checked .el-checkbox__inner::after {
    border-color: #ffffff !important;
  }

  .el-table ::-webkit-scrollbar {
    display: block;
    height: 10px;
  }

  .el-table ::-webkit-scrollbar-thumb {
    background-color: #324157;
  }

  .el-table ::-webkit-scrollbar-thumb:active {
    background-color: #00aff0;
  }

  @media screen and (min-width: 600px) {
    .el-table ::-webkit-scrollbar {
      display: block;
      height: 10px;
    }
  }

  @media screen and (max-width: 600px) {
    .el-table ::-webkit-scrollbar {
      display: none;
    }
  }
</style>

<style scoped>
  .tool-container {
    background: #fff;
  }

  .tool-container-item {
    display: flex;
    margin-bottom: 16px;
  }

  .meta {
    margin: 0.4rem;
    max-width: 350px;
    padding: 0.4rem 0.5rem;
    font-size: 14px;
    font-weight: 600;
    color: #333;
    border: 1px solid #ffd740;
    background-color: #ffd740;
  }
</style>
