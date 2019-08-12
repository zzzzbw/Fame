<template>
  <div>
    <div class="tool-container">
      <el-row>
        <el-col :xs="24" :sm="24" :md="16" :lg="18">
          <el-row>
            <el-col :xs="24" :sm="24" :md="12" :lg="5">
              <div class="tool-container-item">
                <span>
                  状态：
                </span>
                <el-radio-group
                  size="mini"
                  v-model="tool.status"
                  @change="init"
                >
                  <el-radio-button label="">全部</el-radio-button>
                  <el-radio-button
                    :label="this.$static.ArticleStatus.PUBLISH.key"
                    >{{ this.$static.ArticleStatus.PUBLISH.value }}
                  </el-radio-button>
                  <el-radio-button :label="this.$static.ArticleStatus.DRAFT.key"
                    >{{ this.$static.ArticleStatus.DRAFT.value }}
                  </el-radio-button>
                </el-radio-group>
              </div>
            </el-col>
            <el-col :xs="24" :sm="24" :md="12" :lg="5">
              <div class="tool-container-item">
                <span>
                  类型：
                </span>
                <el-radio-group
                  size="mini"
                  v-model="tool.priority"
                  @change="init"
                >
                  <el-radio-button label="">全部</el-radio-button>
                  <el-radio-button :label="this.$static.PostPriority.NORMAL.key"
                    >{{ this.$static.PostPriority.NORMAL.value }}
                  </el-radio-button>
                  <el-radio-button :label="this.$static.PostPriority.TOP.key"
                    >{{ this.$static.PostPriority.TOP.value }}
                  </el-radio-button>
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
                  style="max-width: 350px;"
                  @change="init"
                ></el-input>
              </div>
            </el-col>
            <el-col :xs="24" :sm="24" :md="12" :lg="8">
              <div class="tool-container-item">
                <el-button
                  type="info"
                  size="small"
                  icon="el-icon-edit"
                  @click="handleNew"
                  >新文章
                </el-button>
              </div>
            </el-col>
          </el-row>
        </el-col>
      </el-row>
    </div>

    <el-table :data="postDatas" border stripe style="width: 100%">
      <el-table-column prop="id" label="id" width="60"></el-table-column>
      <el-table-column prop="title" label="标题" show-overflow-tooltip>
        <template slot-scope="scope">
          <el-link :href="scope.row.frontUrl" target="_blank" type="primary"
            >{{ scope.row.title }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column prop="category" label="分类" show-overflow-tooltip>
        <template slot-scope="scope">
          <span :class="{ meta: scope.row.category }">{{
            scope.row.category
          }}</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="status"
        label="状态"
        width="100"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
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
      >
        <template slot-scope="scope">
          <el-tag
            :type="scope.row.priority === '置顶' ? 'warning' : ''"
            effect="dark"
            disable-transitions
            >{{ scope.row.priority }}
          </el-tag>
        </template>
      </el-table-column>
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
            tool: {
                status: '',
                title: '',
                priority: ''
            },
            postDatas: [],
            total: 0,
            pageSize: 10,
            currentPage: 1
        }
    },
    methods: {
        changePage(page) {
            this.currentPage = page;
            this.init();
        },
        handleNew() {
            this.$router.push('/admin/post/publish')
        },
        handleEdit(id) {
            this.$router.push('/admin/post/publish/' + id)
        },
        handleDelete(id) {
            this.$confirm('此操作将永久删除该文章, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'danger'
            }).then(() => {
                this.deletePost(id)
            }).catch(() => {
            })
        },
        initPostDatas(articles) {
            this.postDatas = []
            for (let key in articles) {
                let data = articles[key]
                let post = {
                    id: data.id,
                    title: data.title,
                    frontUrl: this.$serverConfig.frontUrl + 'post/' + data.id,
                    publish: this.$dayjs(data.created).format('YYYY-MM-DD HH:mm'),
                    modified: this.$dayjs(data.modified).format('YYYY-MM-DD HH:mm'),
                    category: data.category,
                    status: this.$static.ArticleStatus.getValue(data.status),
                    priority: this.$static.PostPriority.getValue(data.priority)
                }
                this.postDatas.push(post)
            }
        },
        deletePost(id) {
            this.$api.auth.deletePost(id).then(() => {
                this.$util.message.success('删除成功!')
                this.init()
            })
        },
        init() {
            this.$api.auth.pagePost(this.currentPage, this.tool).then(data => {
                this.initPostDatas(data.data.list)
                this.total = data.data.total
                this.pageSize = data.data.pageSize
            })
        }
    },
    mounted() {
        this.currentPage = Number(this.$route.query.page) || 1
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
