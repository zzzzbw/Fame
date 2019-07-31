<template>
  <div>
    <div class="tool-container">
      <div class="tool-container-item" style="float: right;margin-right: 30px;">
        <el-button
          type="info"
          size="small"
          icon="el-icon-edit"
          @click="handleNew"
          >新页面
        </el-button>
      </div>
    </div>

    <el-table :data="noteDatas" border stripe style="width: 100%">
      <el-table-column prop="id" label="id" width="60"></el-table-column>
      <el-table-column
        prop="title"
        label="标题"
        min-width="150"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column label="发布日期" width="160" show-overflow-tooltip>
        <template slot-scope="scope">
          <i class="el-icon-time"></i>
          <span style="margin-left: 10px">{{ scope.row.publish }}</span>
        </template>
      </el-table-column>
      <el-table-column label="修改日期" width="160" show-overflow-tooltip>
        <template slot-scope="scope">
          <i class="el-icon-time"></i>
          <span style="margin-left: 10px">{{ scope.row.modified }}</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="status"
        label="状态"
        width="100"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column fixed="right" label="操作" width="150">
        <template slot-scope="scope">
          <el-button size="small" @click="handleEdit(scope.row.id)"
            >编辑
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
    <pagination
      @changePage="changePage"
      :pageSize="pageSize"
      :total="total"
    ></pagination>
  </div>
</template>

<script type="text/ecmascript-6">
import Pagination from '../common/Pagination'

export default {
    components: {
        Pagination
    },
    data: function () {
        return {
            noteDatas: [],
            total: 0,
            pageSize: 10,
            currentPage: 1
        }
    },
    methods: {
        changePage(page){
          this.currentPage = page;
          this.init();
        },
        handleNew() {
            this.$router.push('/admin/note/publish')
        },
        handleEdit(id) {
            this.$router.push('/admin/note/publish/' + id)
        },
        handleDelete(id) {
            this.$confirm('此操作将永久删除该自定义页面, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'danger'
            }).then(() => {
                this.deleteNote(id)
            }).catch(() => {
            })
        },
        initNoteDatas(pages) {
            this.noteDatas = []
            for (let key in pages) {
                let data = pages[key]
                let note = {
                    id: data.id,
                    title: data.title,
                    publish: this.$dayjs(data.created).format('YYYY-MM-DD HH:mm'),
                    modified: this.$dayjs(data.modified).format('YYYY-MM-DD HH:mm'),
                    status: this.$static.ArticleStatus.getValue(data.status)
                }
                this.noteDatas.push(note)
            }
        },
        deleteNote(id) {
            this.$api.auth.deleteNote(id).then(() => {
                this.$util.message.success('删除成功!')
                this.init()
            })
        },
        init() {
            this.$api.auth.pageNote(this.currentPage).then(data => {
                this.initNoteDatas(data.data.list)
                this.total = data.data.total
                this.pageSize = data.data.pageSize
            })
        }
    },
    mounted() {
        this.currentPage = Number(this.$route.query.page) || 1;
        this.init()
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
  margin-bottom: 16px;
}

.admin-page {
  margin-top: 30px;
  text-align: center;
}
</style>
