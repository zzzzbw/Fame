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
          <div slot="header">
            <span>最新文章</span>
          </div>
          <ul class="info">
            <li v-for="article in articles" :key="article.id">
              {{ article }}
            </li>
          </ul>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="12" :lg="12">
        <el-card>
          <div slot="header">
            <span>最新评论</span>
          </div>
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

<script>
export default {
  data() {
    return {
      commentCount: 0,
      articleCount: 0,
      comments: [],
      articles: [],
    }
  },
  methods: {
    countComment(data) {
      if (!data.success) {
        return
      }
      this.commentCount = data.data
    },
    countPost(data) {
      if (!data.success) {
        return
      }
      this.articleCount = data.data
    },
    pageComment(data) {
      if (!data.success) {
        return
      }
      for (let key in data.data.list) {
        let comment = data.data.list[key]
        if (comment.content.length > 200) {
          comment.content = comment.content.substring(0, 80) + '...'
        }
        this.comments.push(comment)
      }
    },
    pagePost(data) {
      if (!data.success) {
        return
      }
      for (let key in data.data.list) {
        let d = data.data.list[key]
        let article = d.title
        this.articles.push(article)
      }
    },
    initData(postData, commentData, postCountData, commentCountData) {
      this.pagePost(postData)
      this.pageComment(commentData)
      this.countPost(postCountData)
      this.countComment(commentCountData)
    },
    init() {
      this.$axios
        .all([
          this.$api.auth.pageArticle(1),
          this.$api.auth.pageComment(1),
          this.$api.auth.countArticle(),
          this.$api.auth.countComment(),
        ])
        .then(this.$axios.spread(this.initData))
    },
  },
  mounted() {
    this.init()
  },
}
</script>

<style scoped>
.el-card {
  margin-bottom: 30px;
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
}
</style>
