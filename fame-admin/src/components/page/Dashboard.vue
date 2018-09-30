<template>
  <div>
    <el-row :gutter="50">
      <el-col :xs="24" :sm="12" :md="12" :lg="12">
        <div class="panel">
          <div class="panel-content">
            <div class="message">
              <h3>{{articleCount}}</h3>
              <p>发布文章数</p>
            </div>
            <div class="icon">
              <span class="icon-book"></span>
            </div>
            <div style="clear: both"></div>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="12" :lg="12">
        <div class="panel">
          <div class="panel-content">
            <div class="message">
              <h3>{{commentCount}}</h3>
              <p>评论数</p>
            </div>
            <div class="icon red">
              <span class="icon-comment-o"></span>
            </div>
            <div style="clear: both"></div>
          </div>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="50">
      <el-col :xs="24" :sm="12" :md="12" :lg="12">
        <div class="panel">
          <div class="panel-content">
            <div class="header">
              <div class="title">最新文章</div>
            </div>
            <ul class="info">
              <li v-for="article in articles" :key="article.id">{{article}}</li>
            </ul>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="12" :md="12" :lg="12">
        <div class="panel">
          <div class="panel-content">
            <div class="header">
              <div class="title">最新评论</div>
            </div>
            <ul class="info">
              <li v-for="comment in comments" :key="comment.id">{{comment.name}} => {{comment.content}}</li>
            </ul>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  export default {
    data () {
      return {
        commentCount: 0,
        articleCount: 0,
        comments: [],
        articles: []
      }
    },
    methods: {
      getCommentCount (data) {
        this.commentCount = data.data
      },
      getArticleCount (data) {
        this.articleCount = data.data
      },
      getComments (data) {
        for (let key in data.data.list) {
          let comment = data.data.list[key]
          if (comment.content.length > 200) {
            comment.content = comment.content.substring(0, 80) + '...'
          }
          this.comments.push(comment)
        }
      },
      getArticle (data) {
        for (let key in data.data.list) {
          let d = data.data.list[key]
          let article = d.title
          this.articles.push(article)
        }
      },
      initData (articlesData, logsData, articleCountData, commentCountData) {
        this.getArticleCount(articleCountData)
        this.getComments(logsData)
        this.getArticle(articlesData)
        this.getCommentCount(commentCountData)
      },
      init () {
        this.$axios.all([this.$api.auth.getArticles(1), this.$api.auth.getComments(1),
          this.$api.auth.getArticleCount(), this.$api.auth.getCommentCount()])
          .then(this.$axios.spread(this.initData))
      }
    },
    mounted () {
      this.init()
    }
  }
</script>

<style scoped>
</style>
