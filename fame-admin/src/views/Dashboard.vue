<template>
  <div>
    <el-row :gutter="40">
      <el-col :xs="24" :sm="12" :md="12" :lg="12">
        <el-card>
          <div class="message">
            <h3>{{ articleCount }}</h3>
            <p>发布文章数</p>
          </div>
          <el-icon size="18" color="#ffffff" class="icon-message blue">
            <Postcard />
          </el-icon>
          <div style="clear: both"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="12" :lg="12">
        <el-card>
          <div class="message">
            <h3>{{ commentCount }}</h3>
            <p>评论数</p>
          </div>
          <el-icon size="18" color="#ffffff" class="icon-message red">
            <ChatLineSquare />
          </el-icon>
          <div style="clear: both"></div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="40">
      <el-col :xs="24" :sm="12" :md="12" :lg="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>最新文章</span>
            </div>
          </template>
          <ul class="info">
            <li v-for="article in articles" :key="article.id">
              {{ article.title }}
            </li>
          </ul>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="12" :lg="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>最新评论</span>
            </div>
          </template>
          <ul class="info">
            <li v-for="comment in comments" :key="comment.id">
              {{ comment.name }} => {{ comment.content }}
            </li>
          </ul>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
  import { ref, reactive, onMounted } from 'vue'
  import { Postcard, ChatLineSquare } from '@element-plus/icons-vue'
  import { RestResponse, Page } from '~/types/common'
  import { handleRestResponse } from '~/utils'
  import { Api } from '~/api'
  import axios from 'axios'
  import { BaseArticle } from '~/types/article'
  import { BaseComment } from '~/types/comment'

  const articleCount = ref(0)
  const commentCount = ref(0)

  const articles = reactive<Array<BaseArticle>>([])
  const comments = reactive<Array<BaseComment>>([])

  const initArticleData = (resp: RestResponse<Page<BaseArticle>>) => {
    handleRestResponse(resp, (page) => {
      for (let key in page.list) {
        let article = page.list[key]
        articles.push(article)
      }
    })
  }

  const initCommentData = (resp: RestResponse<Page<BaseComment>>) => {
    handleRestResponse(resp, (page) => {
      for (let key in page.list) {
        let comment = page.list[key]

        if (comment.content?.length && comment.content.length > 200) {
          comment.content = comment.content?.substring(0, 80) + '...'
        }
        comments.push(comment)
      }
    })
  }

  const initArticleCount = (resp: RestResponse<number>) => {
    handleRestResponse(resp, (count) => {
      articleCount.value = count
    })
  }

  const initCommentCount = (resp: RestResponse<number>) => {
    handleRestResponse(resp, (count) => {
      commentCount.value = count
    })
  }

  onMounted(() => {
    axios
      .all([Api.pageArticle(1, 10), Api.pageComment(1, 10), Api.countArticle(), Api.countComment()])
      .then(
        axios.spread((articleData, commentData, articleCount, commentCount) => {
          initArticleData(articleData as RestResponse<Page<BaseArticle>>)
          initCommentData(commentData as RestResponse<Page<BaseComment>>)
          initArticleCount(articleCount as RestResponse<number>)
          initCommentCount(commentCount as RestResponse<number>)
        })
      )
  })
</script>

<style lang="scss" scoped>
  @import '~/assets/css/admin.scss';
  .el-card {
    margin-bottom: 30px;
  }

  .card-header {
    @include layout(space-between, center);
  }

  .message {
    float: left;
  }

  .message h3 {
    font-size: 32px;
    text-align: left;
    color: #676767;
    margin: 0;
  }

  .message p {
    font-size: 14px;
    color: #aab5bc;
    font-weight: 600;
    text-transform: uppercase;
    margin: 8px 5px;
    display: inline-block;
  }

  .icon-message {
    font-size: 30px;
    color: #ffffff;
    padding: 19px;
    float: right;
    border-radius: 6px;
  }

  .info {
    padding-left: 0;
    list-style: none;
  }

  .info li {
    color: #555;
    display: block;
    padding: 10px 15px;
    margin-bottom: -1px;
    background-color: #fff;
    border: 1px solid #ddd;
    text-align: left;
  }
</style>
