<template>
  <div>
    <el-row :gutter="40">
      <el-col :xs="24" :sm="12" :md="12" :lg="12">
        <el-card>
          <div class="message">
            <h3>{{ articleCount }}</h3>
            <p>发布文章数</p>
          </div>
          <div class="icon">
            <i class="el-icon-tickets"></i>
          </div>
          <div style="clear: both"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="12" :lg="12">
        <el-card>
          <div class="message">
            <h3>{{ commentCount }}</h3>
            <p>评论数</p>
          </div>
          <div class="icon red">
            <i class="el-icon-chat-line-round"></i>
          </div>
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

<script lang="ts">
  import { defineComponent, ref, reactive } from 'vue'
  import { RestResponse, Pagination } from '~/types'
  import { Api } from '~/api'
  import axios from 'axios'

  interface Article {
    id: number
    title: string
  }

  interface Comment {
    id: number
    name: string
    content: string
  }

  export default defineComponent({
    setup() {
      const articleCount = ref(1)
      const commentCount = ref(12)

      const articles = reactive(Array<Article>())
      const comments = reactive(Array<Comment>())

      const initArticleData = (resp: RestResponse<Pagination<Article>>) => {
        if (!resp.success) {
          console.log(resp)
          return
        }
        for (let key in resp.data.list) {
          let article = resp.data.list[key]
          articles.push(article)
        }
      }

      const initCommentData = (resp: RestResponse<Pagination<Comment>>) => {
        if (!resp.success) {
          console.log(resp)
          return
        }
        for (let key in resp.data.list) {
          let comment = resp.data.list[key]
          if (comment.content.length > 200) {
            comment.content = comment.content.substring(0, 80) + '...'
          }
          comments.push(comment)
        }
      }

      const initArticleCount = (resp: RestResponse<number>) => {
        if (!resp.success) {
          console.log(resp)
          return
        }
        articleCount.value = resp.data
      }

      const initCommentCount = (resp: RestResponse<number>) => {
        if (!resp.success) {
          console.log(resp)
          return
        }
        commentCount.value = resp.data
      }

      axios
        .all([Api.pageArticle(1), Api.pageComment(1), Api.countArticle(), Api.countComment()])
        .then(
          axios.spread((articleData, commentData, articleCount, commentCount) => {
            initArticleData(articleData as RestResponse<Pagination<Article>>)
            initCommentData(commentData as RestResponse<Pagination<Comment>>)
            initArticleCount(articleCount as RestResponse<number>)
            initCommentCount(commentCount as RestResponse<number>)
          })
        )

      return {
        articleCount,
        commentCount,
        articles,
        comments
      }
    }
  })
</script>

<style scoped>
  .el-card {
    margin-bottom: 30px;
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .message {
    float: left;
  }

  .message h3 {
    font-size: 32px;
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

  .icon {
    font-size: 30px;
    color: #ffffff;
    background: #30a5ff;
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
