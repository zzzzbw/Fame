<template>
  <div>
    <el-form label-position="top" :rules="rules" ref="postForm" :model="post">
      <el-row :gutter="30">
        <el-col :xs="24" :sm="16" :md="19" :lg="19">
          <el-form-item prop="title">
            <el-input
              v-model="post.title"
              placeholder="请输入文章标题"
            ></el-input>
          </el-form-item>
          <el-form-item prop="content">
            <markdown-editor v-model="post.content" :onSave="onSave" />
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
                  v-model="post.category"
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
              <el-form-item>
                <el-switch
                  v-model="post.status"
                  :active-value="this.$static.ArticleStatus.PUBLISH.key"
                  :inactive-value="this.$static.ArticleStatus.DRAFT.key"
                  :active-text="this.$static.ArticleStatus.PUBLISH.value"
                  :inactive-text="this.$static.ArticleStatus.DRAFT.value"
                >
                </el-switch>
              </el-form-item>
              <el-form-item>
                <el-switch
                  v-model="post.priority"
                  :active-value="this.$static.PostPriority.TOP.key"
                  :inactive-value="this.$static.PostPriority.NORMAL.key"
                  active-color="#ffd740"
                  :active-text="this.$static.PostPriority.TOP.value"
                  :inactive-text="this.$static.PostPriority.NORMAL.value"
                >
                </el-switch>
              </el-form-item>
              <el-form-item>
                <el-switch
                  v-model="post.allowComment"
                  active-color="#13ce66"
                  active-text="开启评论"
                  inactive-text="关闭"
                >
                </el-switch>
              </el-form-item>
              <el-form-item label="创建日期">
                <el-date-picker
                  v-model="post.created"
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
                  v-model="post.modified"
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
                      v-if="this.post.id !== ''"
                    >
                      <a
                        :href="
                          this.$serverConfig.frontUrl + 'post/' + this.post.id
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
      post: {
        id: "",
        title: "",
        tags: "",
        category: "",
        content: "",
        status: "",
        priority: 0,
        allowComment: true,
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
    getPost() {
      const id = this.$route.params.id;
      // 如果有id则表示编辑文章,获取文章信息
      if (id) {
        this.$api.auth.getPost(id).then(data => {
          this.initPost(data.data);
        });
      } else {
        // 如果没有id则表示新增文章,初始化文章信息
        const data = {
          id: "",
          title: "",
          tags: "",
          category: "",
          content: "",
          status: this.$static.ArticleStatus.PUBLISH.key,
          priority: this.$static.PostPriority.NORMAL.key,
          allowComment: true,
          created: Date.now(),
          modified: Date.now()
        };
        this.initPost(data);
      }
    },
    initPost(data) {
      this.post.id = data.id;
      this.post.title = data.title;
      this.post.tags = data.tags;
      this.post.category = data.category;
      this.post.content = data.content;
      this.post.status = data.status;
      this.post.priority = data.priority;
      this.post.allowComment = data.allowComment;
      this.post.created = new Date(data.created).getTime();
      this.post.modified = Date.now();
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
      this.$api.auth.pageMedia(12, page).then(data => {
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
          .pageMedia(12, this.mediaDialogData.currentPage)
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
    submitPost(formName, success) {
      if (this.submitting) {
        this.$util.message.warning("请不要提交过快!");
        return;
      }
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.submitting = true;
          let params = this.post;
          params.tags = this.$util.tagsToString(this.selectTags);
          this.$api.auth.savePost(params).then(data => {
            if (data.success) {
              success(data.data);
            } else {
              this.$util.message.error("提交文章失败," + data.msg);
            }
            this.submitting = false;
          });
        }
      });
    },
    onPublish() {
      const _this = this;
      this.submitPost("postForm", function() {
        _this.$util.message.success("发布文章成功!");
        _this.$router.push("/post");
      });
    },
    onSave() {
      const _this = this;
      this.submitPost("postForm", function(data) {
        _this.$util.message.success("保存文章成功!");
        _this.$route.params.id = data;
        _this.getPost();
      });
    },
    init() {
      this.getPost();
      this.getTags();
      this.getCategories();
    }
  },
  mounted() {
    this.init();
  },
  watch: {
    // 监听route刷新绑定的post数据
    $route() {
      this.getPost();
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
