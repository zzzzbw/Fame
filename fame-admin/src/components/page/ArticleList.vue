<template>
  <div>
    <div class="tool-container">
      <el-row>
        <el-col :xs="24" :sm="24" :md="16" :lg="18">
          <el-row>
            <el-col :xs="24" :sm="24" :md="12" :lg="5">
              <div class="tool-container-item">
                <span>状态：</span>
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
                <span>类型：</span>
                <el-radio-group
                  size="mini"
                  v-model="tool.priority"
                  @change="init"
                >
                  <el-radio-button label="">全部</el-radio-button>
                  <el-radio-button
                    :label="this.$static.ArticlePriority.NORMAL.key"
                    >{{ this.$static.ArticlePriority.NORMAL.value }}
                  </el-radio-button>
                  <el-radio-button :label="this.$static.ArticlePriority.TOP.key"
                    >{{ this.$static.ArticlePriority.TOP.value }}
                  </el-radio-button>
                </el-radio-group>
              </div>
            </el-col>
            <el-col :xs="24" :sm="24" :md="12" :lg="5">
              <div class="tool-container-item">
                <span>列表显示：</span>
                <el-radio-group
                  size="mini"
                  v-model="tool.listShow"
                  @change="init"
                >
                  <el-radio-button :label="null">全部</el-radio-button>
                  <el-radio-button :label="true">是</el-radio-button>
                  <el-radio-button :label="false">否</el-radio-button>
                </el-radio-group>
              </div>
            </el-col>
            <el-col :xs="24" :sm="24" :md="12" :lg="5">
              <div class="tool-container-item">
                <span>顶部显示：</span>
                <el-radio-group
                  size="mini"
                  v-model="tool.headerShow"
                  @change="init"
                >
                  <el-radio-button :label="null">全部</el-radio-button>
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

    <el-table :data="articleData" border stripe style="width: 100%">
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
        prop="listShow"
        label="列表显示"
        width="80"
        show-overflow-tooltip
        align="center"
      >
        <template slot-scope="scope">
          <i
            v-if="scope.row.listShow"
            class="el-icon-check"
            style="font-size: 24px; color: #409eff"
          ></i>
          <i v-else class="el-icon-minus" style="font-size: 24px"></i>
        </template>
      </el-table-column>
      <el-table-column
        prop="headerShow"
        label="顶部显示"
        width="80"
        show-overflow-tooltip
        align="center"
      >
        <template slot-scope="scope">
          <i
            v-if="scope.row.headerShow"
            class="el-icon-check"
            style="font-size: 24px; color: #409eff"
          ></i>
          <i v-else class="el-icon-minus" style="font-size: 24px"></i>
        </template>
      </el-table-column>
      <el-table-column
        prop="status"
        label="状态"
        width="100"
        show-overflow-tooltip
        align="center"
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
        align="center"
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
      @change-page="changePage"
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
                priority: '',
                listShow: null,
                headerShow: null,
            },
            articleData: [],
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
            this.$router.push('/article/publish')
        },
        handleEdit(id) {
            this.$router.push('/article/publish/' + id)
        },
        handleDelete(id) {
            this.$confirm('此操作将永久删除该文章, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'danger'
            }).then(() => {
                this.deleteArticle(id)
            }).catch(() => {
            })
        },
        initArticleData(articles) {
            this.articleData = []
            for (let key in articles) {
                if(!articles.hasOwnProperty(key)){
                    continue
                }
                let data = articles[key]
                let article = {
                    id: data.id,
                    title: data.title,
                    frontUrl: this.$util.getServerFrontPostUrl(data.id),
                    publish: this.$dayjs(data.created).format('YYYY-MM-DD HH:mm'),
                    modified: this.$dayjs(data.modified).format('YYYY-MM-DD HH:mm'),
                    category: data.category ? data.category.name : '',
                    listShow: data.listShow,
                    headerShow: data.headerShow,
                    status: this.$static.ArticleStatus.getValue(data.status),
                    priority: this.$static.ArticlePriority.getValue(data.priority)
                }
                this.articleData.push(article)
            }
        },
        deleteArticle(id) {
            this.$api.auth.deleteArticle(id).then(() => {
                this.$util.message.success('删除成功!')
                this.init()
            })
        },
        init() {
            this.$api.auth.pageArticle(this.currentPage, this.tool).then(data => {
                this.initArticleData(data.data.list)
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
