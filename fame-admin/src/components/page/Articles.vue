<template>
  <div>
    <el-table :data="articleDatas" border stripe style="width: 100%">
      <el-table-column prop="id" label="id" width="60"></el-table-column>
      <el-table-column prop="title" label="标题" show-overflow-tooltip></el-table-column>
      <el-table-column prop="category" label="分类" show-overflow-tooltip></el-table-column>
      <el-table-column prop="status" label="状态" width="100" show-overflow-tooltip></el-table-column>
      <el-table-column prop="publish" label="发布日期" width="150" show-overflow-tooltip></el-table-column>
      <el-table-column prop="modified" label="修改日期" width="150" show-overflow-tooltip></el-table-column>
      <el-table-column fixed="right" label="操作" width="150">
        <template slot-scope="scope">
          <el-button
              size="small"
              @click="handleEdit(scope.row.id)">编辑
          </el-button>
          <el-button
              size="small"
              type="danger"
              @click="handleDelete(scope.row.id)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="admin-page">
      <el-pagination
          layout="total,prev, pager, next"
          @current-change="init"
          :current-page.sync="currentPage"
          :page-size="pageSize"
          :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  export default {
    data: function () {
      return {
        articleDatas: [],
        total: 0,
        pageSize: 10,
        currentPage: 1
      }
    },
    methods: {
      handleEdit (id) {
        this.$router.push('/admin/article/publish/' + id)
      },
      handleDelete (id) {
        this.$confirm('此操作将永久删除该文章, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'danger'
        }).then(() => {
          this.deleteArticle(id)
        }).catch(() => {
        })
      },
      initArticleDatas (articles) {
        this.articleDatas = []
        for (let key in articles) {
          let data = articles[key]
          let article = {
            id: data.id,
            title: data.title,
            publish: this.$dayjs(data.created).format('YYYY-MM-DD HH:mm'),
            modified: this.$dayjs(data.modified).format('YYYY-MM-DD HH:mm'),
            category: data.category || this.$util.STATIC.DEFAULT_CATEGORY,
            status: this.$util.STATIC.STATUS_PUBLISH === data.status ? '公开' : '隐藏'
          }
          this.articleDatas.push(article)
        }
      },
      deleteArticle (id) {
        this.$api.auth.deleteArticle(id).then(() => {
          this.$message({
            type: 'success',
            message: '删除成功!'
          })
          this.init(this.$route.query.page)
        })
      },
      init (page) {
        this.$api.auth.getArticles(page || 1).then(data => {
          this.initArticleDatas(data.data.list)
          this.total = data.data.total
          this.pageSize = data.data.pageSize
          this.currentPage = Number(page) || 1
        })
      }
    },
    mounted () {
      this.init(this.$route.query.page)
    }
  }
</script>

<style>
  .el-table ::-webkit-scrollbar {
    display: block;
    height: 10px;
  }

  .el-table ::-webkit-scrollbar-thumb {
    background-color: #324157;
  }

  .el-table ::-webkit-scrollbar-thumb:active {
    background-color: #00aff0
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
  .el-table {
    border: 1px solid #e6ebf5;
  }

  .admin-page {
    margin-top: 30px;
    text-align: center;
  }
</style>
