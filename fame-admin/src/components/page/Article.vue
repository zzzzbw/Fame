<template>
  <div>
    <el-form
      label-position="top"
      :rules="rules"
      ref="articleForm"
      :model="article"
    >
      <el-row :gutter="30">
        <el-col :xs="24" :sm="16" :md="19" :lg="19">
          <el-form-item prop="title">
            <el-input
              v-model="article.title"
              placeholder="请输入文章标题"
            ></el-input>
          </el-form-item>
          <el-form-item prop="content">
            <markdown-editor
              v-model="article.content"
              :onSave="onSave"
              @keydown.native.meta.83="onSave"
            />
            <!-- 键修饰符，键别名 -->
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="8" :md="5" :lg="5">
          <div class="panel">
            <div class="panel-content">
              <el-form-item label="标签">
                <el-select
                  v-model="selectTags"
                  multiple
                  filterable
                  placeholder="请选择文章标签"
                >
                  <el-option
                    v-for="tag in tags"
                    :key="tag.value"
                    :label="tag.label"
                    :value="tag.value"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="分类">
                <el-select
                  v-model="article.category"
                  filterable
                  placeholder="请选择文章分类"
                >
                  <el-option
                    v-for="category in categories"
                    :key="category.value"
                    :label="category.label"
                    :value="category.value"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="状态">
                <el-switch
                  v-model="article.status"
                  active-value="publish"
                  inactive-value="draft"
                  active-text="公开"
                  inactive-text="隐藏"
                >
                </el-switch>
              </el-form-item>
              <el-form-item label="创建日期">
                <el-date-picker
                  v-model="article.created"
                  type="datetime"
                  placeholder="创建日期"
                  size="small"
                  :editable="flagFalse"
                  value-format="timestamp"
                >
                </el-date-picker>
              </el-form-item>
              <el-form-item label="修改日期">
                <el-date-picker
                  v-model="article.modified"
                  type="datetime"
                  placeholder="修改日期"
                  size="small"
                  :editable="flagFalse"
                  value-format="timestamp"
                >
                </el-date-picker>
              </el-form-item>
              <el-form-item>
                <el-button size="small" @click="showMediaDialog(1)"
                  >媒体库
                </el-button>
              </el-form-item>
              <el-form-item>
                <el-button-group>
                  <el-row>
                    <el-button type="success" size="small" @click="onSave"
                      >保存
                    </el-button>
                    <el-button type="primary" size="small" @click="onPublish"
                      >发布
                    </el-button>
                    <el-button
                      type="info"
                      size="small"
                      v-if="this.article.id !== ''"
                    >
                      <a
                        :href="
                          this.$serverConfig.frontUrl +
                            'article/' +
                            this.article.id
                        "
                        target="_blank"
                        style="color: #FFFFFF;"
                        >查看</a
                      >
                    </el-button>
                  </el-row>
                </el-button-group>
              </el-form-item>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-form>
    <el-dialog
      :visible.sync="mediaDialog"
      top="10vh"
      :fullscreen="isMobile"
      append-to-body
      center
      width="80%"
    >
      <upload :afterUpload="afterUpload"></upload>
      <div class="media-list">
        <el-row>
          <el-col
            style="padding: 6px"
            :xs="24"
            :sm="12"
            :md="12"
            :lg="6"
            :xl="4"
            v-for="media in mediaDialogData.mediaDatas"
            :key="media.id"
          >
            <media-item
              :media="media"
              :after-delete="afterDeleteMedia"
            ></media-item>
          </el-col>
        </el-row>
        <div class="admin-page">
          <el-pagination
            layout="total, prev, pager, next"
            @current-change="showMediaDialog"
            :current-page.sync="mediaDialogData.currentPage"
            :page-size="mediaDialogData.pageSize"
            :total="mediaDialogData.total"
          >
          </el-pagination>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import MarkdownEditor from "../common/MarkdownEditor";
import MediaItem from "../common/MediaItem";
import Upload from "../common/Upload";

export default {
  components: {
    MarkdownEditor,
    MediaItem,
    Upload
  },
  data: function() {
    return {
      mediaDialog: false,
      isMobile: false,
      submitting: false,
      article: {
        id: "",
        title: "",
        tags: "",
        category: "",
        content: "",
        status: "",
        created: "",
        modified: ""
      },
      rules: {
        title: [
          { required: true, message: "文章标题必须输入", trigger: "blur" }
        ],
        content: [
          { required: true, message: "文章内容不能为空", trigger: "blur" }
        ]
      },
      selectTags: [],
      tags: [],
      categories: [],
      flagFalse: false,
      mediaDialogData: {
        mediaDatas: [],
        total: 0,
        pageSize: 10,
        currentPage: 1
      }
    };
  },
  methods: {
    getArticle() {
      const id = this.$route.params.id;
      // 如果有id则表示编辑文章,获取文章信息
      if (id) {
        this.$api.auth.getArticle(id).then(data => {
          this.initArticle(data.data);
        });
      } else {
        // 如果没有id则表示新增文章,不用清空文章信息
        const data = {
          id: "",
          title: "",
          tags: "",
          category: "",
          content: "",
          status: this.$util.STATIC.STATUS_PUBLISH,
          created: Date.now(),
          modified: Date.now()
        };
        this.initArticle(data);
      }
    },
    initArticle(data) {
      this.article.id = data.id;
      this.article.title = data.title;
      this.article.tags = data.tags;
      this.article.category = data.category;
      this.article.content = data.content;
      this.article.status = data.status;
      this.article.created = new Date(data.created).getTime();
      this.article.modified = Date.now();
      this.selectTags = this.$util.stringToTags(data.tags);
    },
    getTags() {
      this.$api.auth.getAllTags().then(data => {
        if (data.success) {
          for (let key in data.data) {
            let tag = {
              value: data.data[key].name,
              label: data.data[key].name
            };
            this.tags.push(tag);
          }
        } else {
          this.$util.message.error("获取标签列表失败");
        }
      });
    },
    getCategories() {
      this.$api.auth.getAllCategories().then(data => {
        if (data.success) {
          for (let key in data.data) {
            let category = {
              value: data.data[key].name,
              label: data.data[key].name
            };
            this.categories.push(category);
          }
        } else {
          this.$util.message.error("获取分类列表失败");
        }
      });
    },
    showMediaDialog(page = 1) {
      this.isMobile = document.body.clientWidth < 768;
      this.mediaDialog = true;
      this.$api.auth.getMedias(12, page).then(data => {
        this.mediaDialogData.mediaDatas = data.data.list;
        this.mediaDialogData.total = data.data.total;
        this.mediaDialogData.pageSize = data.data.pageSize;
        for (let media of this.mediaDialogData.mediaDatas) {
          if (media.thumbUrl && media.thumbUrl !== "") {
            media.showUrl = this.$util.getServerMediaUrl(media.thumbUrl);
          } else {
            media.showUrl = this.$util.getServerMediaUrl(media.url);
          }
        }
      });
    },
    afterDeleteMedia(data) {
      if (data.success) {
        this.showMediaDialog(1);
      }
    },
    afterUpload(response) {
      if (response.success) {
        this.$api.auth
          .getMedias(12, this.mediaDialogData.currentPage)
          .then(data => {
            this.mediaDialogData.mediaDatas = data.data.list;
            this.mediaDialogData.total = data.data.total;
            this.mediaDialogData.pageSize = data.data.pageSize;
            for (let media of this.mediaDialogData.mediaDatas) {
              if (media.thumbUrl && media.thumbUrl !== "") {
                media.showUrl = this.$util.getServerMediaUrl(media.thumbUrl);
              } else {
                media.showUrl = this.$util.getServerMediaUrl(media.url);
              }
            }
          });
      }
    },
    submitArticle(formName, success) {
      if (this.submitting) {
        this.$util.message.warning("请不要提交过快!");
        return;
      }
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.submitting = true;
          let params = this.article;
          params.tags = this.$util.tagsToString(this.selectTags);
          this.$api.auth.saveArticle(params).then(data => {
            if (data.success) {
              success(data.data);
            } else {
              this.$util.error("提交文章失败文章失败," + data.msg);
            }
            this.submitting = false;
          });
        }
      });
    },
    onPublish() {
      const _this = this;
      this.submitArticle("articleForm", function() {
        _this.$util.message.success("发布文章成功!");
        _this.$router.push("/admin/article");
      });
    },
    onSave() {
      const _this = this;
      this.submitArticle("articleForm", function(data) {
        _this.$util.message.success("保存文章成功!");
        _this.$route.params.id = data;
        _this.getArticle();
      });
    },
    init() {
      this.getArticle();
      this.getTags();
      this.getCategories();
    }
  },
  mounted() {
    this.init();
  },
  watch: {
    // 监听route刷新绑定的article数据
    $route() {
      this.getArticle();
    }
  }
};
</script>

<style scoped>
.el-select {
  width: 100%;
}

.el-date-editor {
  width: 100%;
}

.admin-page {
  margin-top: 30px;
  text-align: center;
}

a {
  text-decoration: none;
}
</style>
