<template>
  <div>
    <el-table :data="commentList" border style="width: 100%">
      <el-table-column prop="id" label="id" width="60"></el-table-column>
      <el-table-column prop="name" label="称呼" show-overflow-tooltip></el-table-column>
      <el-table-column prop="content" label="内容" show-overflow-tooltip></el-table-column>
      <el-table-column prop="email" label="邮箱" show-overflow-tooltip></el-table-column>
      <el-table-column prop="" label="评论日期" width="160" show-overflow-tooltip>
        <template #default="scope">
          <i class="el-icon-time"></i>
          <span style="margin-left: 10px">{{ scope.row.created }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="scope">
          <el-button size="small" @click="handleDetail(scope.row.id)">详情 </el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row.id)"
            >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog
      v-model:visible="detailVisible"
      :modal="true"
      :fullscreen="isMobile"
      :width="dialogWidth"
      center
      append-to-body
      custom-class="comment-dialog"
    >
      <el-row :gutter="10">
        <p class="comment-article-title">
          文章:
          <el-link :href="comment.postUrl" target="_blank" type="primary">{{
            comment.title
          }}</el-link>
        </p>
      </el-row>
      <el-row :gutter="10" class="comment-row-detail">
        <el-col :xs="24" :sm="2" :md="2">称呼:</el-col>
        <el-col :xs="24" :sm="6" :md="6">{{ comment.name }}</el-col>
        <el-col :xs="24" :sm="2" :md="2">邮箱:</el-col>
        <el-col :xs="24" :sm="6" :md="6">{{ comment.email }}</el-col>
        <el-col :xs="24" :sm="2" :md="2">网址:</el-col>
        <el-col :xs="24" :sm="6" :md="6">{{ comment.website }}</el-col>
      </el-row>
      <el-row :gutter="10" class="comment-row-detail">
        <el-col :span="24">
          <div v-show="hasReplay" class="markdown-body comment-replay">
            <div>
              <span class="comment-replay-name">{{ comment.replayName }}</span>
            </div>
            <div v-html="comment.replay"></div>
          </div>
          <div class="markdown-body" v-html="comment.content"></div>
        </el-col>
      </el-row>
      <el-row :gutter="10" class="comment-row-detail">
        <el-col :xs="24" :sm="12" :md="3">赞:</el-col>
        <el-col :xs="24" :sm="12" :md="9">{{ comment.agree }}</el-col>
        <el-col :xs="24" :sm="12" :md="3">踩:</el-col>
        <el-col :xs="24" :sm="12" :md="9">{{ comment.disagree }}</el-col>
        <el-col :xs="24" :sm="12" :md="3">ip:</el-col>
        <el-col :xs="24" :sm="12" :md="9">{{ comment.ip }}</el-col>
        <el-col :xs="24" :sm="12" :md="3">agent:</el-col>
        <el-col :xs="24" :sm="12" :md="9">{{ comment.agent }}</el-col>
      </el-row>
    </el-dialog>

    <pagination :total="total" :page-size="pageSize" @handle-current-change="changePage" />
  </div>
</template>

<script lang="ts">
  import { defineComponent, ref, reactive, onMounted } from 'vue'
  import { RestResponse, Pagination } from '~/types'
  import { handleRestResponse } from '~/utils'
  import { Api } from '~/api'

  interface CommentListItem {
    id: number
    name: string
    content: string
    email: string
    created: string
  }

  export default defineComponent({
    setup() {
      const commentList = reactive<Array<CommentListItem>>([])
      const comment = reactive({})
      const currentPage = ref(1)
      const total = ref(0)
      const pageSize = ref(10)

      async function getCommentData() {
        const resp = (await Api.pageComment(currentPage.value)) as RestResponse<
          Pagination<CommentListItem>
        >
        handleRestResponse(resp, (page) => {
          total.value = page.total
          pageSize.value = page.pageSize
          commentList.splice(0)
          for (let key in page.list) {
            let comment = page.list[key]
            commentList.push(comment)
          }
        })
      }

      const changePage = (newPage: number) => {
        currentPage.value = newPage
        getCommentData()
      }

      onMounted(() => {
        getCommentData()
      })

      return {
        currentPage,
        total,
        pageSize,
        commentList,
        comment,
        changePage
      }
    }
  })
</script>

<style lang="scss" scoped>
  .comment-dialog .comment-article-title {
    font-size: 24px;
    width: 85%;
    margin: auto !important;
  }

  .comment-dialog .comment-article-title a {
    font-size: 20px;
    text-decoration: underline;
  }

  .comment-dialog .comment-row-detail {
    width: 90%;
    margin: 15px auto !important;
    padding: 10px;
    border: 1px solid #eee;
    border-radius: 4px;
  }

  .comment-dialog .comment-row-detail .el-col {
    text-align: left !important;
  }

  .comment-dialog .comment-row-detail .comment-replay {
    padding: 10px;
    margin-bottom: 5px;
    border: 1px solid #eee;
    border-radius: 4px;
  }

  .comment-replay .comment-replay-name {
    text-decoration: underline;
    font-size: 16px;
  }

  @media screen and (min-width: 768px) {
    .comment-dialog .el-row .el-col:nth-child(odd) {
      text-align: right;
    }
  }
</style>
