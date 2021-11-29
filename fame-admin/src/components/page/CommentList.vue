<template>
  <div>
    <el-table :data="commentDatas" border style="width: 100%">
      <el-table-column prop="id" label="id" width="60"></el-table-column>
      <el-table-column
        prop="name"
        label="称呼"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="content"
        label="内容"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="email"
        label="邮箱"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop=""
        label="评论日期"
        width="160"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <i class="el-icon-time"></i>
          <span style="margin-left: 10px">{{ scope.row.created }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template slot-scope="scope">
          <el-button size="small" @click="handleDetail(scope.row.id)"
            >详情
          </el-button>
          <el-button
            size="small"
            type="danger"
            @click="handleDelete(scope.row.id)"
            >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog
      :visible.sync="detailVisible"
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
          <div class="markdown-body comment-replay" v-show="hasReplay">
            <div>
              <span class="comment-replay-name">{{ comment.replayName }}</span>
            </div>
            <div v-html="comment.replay"></div>
          </div>
          <div v-html="comment.content" class="markdown-body"></div>
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
    <pagination
      @change-page="changePage"
      :pageSize="pageSize"
      :total="total"
    ></pagination>
  </div>
</template>

<script>
import Pagination from '@/components/common/Pagination'

export default {
  components: {
    Pagination,
  },
  data: function () {
    return {
      commentDatas: [],
      comment: {},
      total: 0,
      pageSize: 10,
      currentPage: 1,
      detailVisible: false,
      hasReplay: false,
      isMobile: false,
      dialogWidth: '60%',
    }
  },
  methods: {
    changePage(page) {
      this.currentPage = page
      this.init()
    },
    init() {
      this.$api.auth.pageComment(this.currentPage).then((data) => {
        this.commentDatas = data.data.list
        this.total = data.data.total
        this.pageSize = data.data.pageSize
        for (let comment of this.commentDatas) {
          comment.created = this.$dayjs(comment.created).format(
            'YYYY-MM-DD HH:mm'
          )
        }
      })
    },
    initDetail(data) {
      this.comment = data
      if (data.article) {
        this.comment.title = data.article.title
        this.comment.postUrl = this.$util.getServerFrontPostUrl(data.article.id)
      }
      if (data.parentComment) {
        this.hasReplay = true
        this.comment.replayName = data.parentComment.name
        this.comment.replay = data.parentComment.content
      } else {
        this.hasReplay = false
      }
    },
    handleDetail(id) {
      this.$api.auth.getCommentDetail(id).then((data) => {
        this.initDetail(data.data)
        this.detailVisible = true
        if (document.body.clientWidth < 768) {
          this.isMobile = true
          this.dialogWidth = '100%'
        } else {
          this.isMobile = false
          this.dialogWidth = '60%'
        }
      })
    },
    handleDelete(id) {
      this.$confirm('此操作将永久删除该评论,是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'danger',
      })
        .then(() => {
          this.deleteComment(id)
        })
        .catch(() => {})
    },
    deleteComment(id) {
      this.$api.auth.deleteComment(id).then((data) => {
        if (data.success) {
          this.$util.message.success('删除成功!')
          this.init()
        } else {
          this.$util.message.error('删除失败 ' + data.msg)
        }
      })
    },
  },
  mounted() {
    this.currentPage = Number(this.$route.query.page) || 1
    this.init()
  },
}
</script>

<style scoped>
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
