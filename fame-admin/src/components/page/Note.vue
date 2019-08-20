<template>
  <div>
    <el-form :rules="rules" ref="noteForm" :model="note">
      <el-row :gutter="30">
        <el-col :xs="24" :sm="16" :md="19" :lg="19">
          <el-form-item prop="title">
            <el-input
              v-model="note.title"
              placeholder="请输入自定义页面标题"
            ></el-input>
          </el-form-item>
          <el-form-item prop="content">
            <markdown-editor v-model="note.content" />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="8" :md="5" :lg="5">
          <div class="panel">
            <div class="panel-content">
              <el-form-item>
                <el-switch
                  v-model="note.status"
                  :active-value="this.$static.ArticleStatus.PUBLISH.key"
                  :inactive-value="this.$static.ArticleStatus.DRAFT.key"
                  :active-text="this.$static.ArticleStatus.PUBLISH.value"
                  :inactive-text="this.$static.ArticleStatus.DRAFT.value"
                >
                </el-switch>
              </el-form-item>
              <el-form-item label="排序权重">
                <el-input-number
                  v-model="note.priority"
                  :min="0"
                  size="mini"
                  controls-position="right"
                ></el-input-number>
              </el-form-item>
              <el-form-item>
                <el-switch
                  v-model="note.allowComment"
                  active-color="#13ce66"
                  active-text="开启评论"
                  inactive-text="关闭"
                >
                </el-switch>
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
                  </el-row>
                </el-button-group>
              </el-form-item>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-form>
  </div>
</template>

<script>
import MarkdownEditor from "../common/MarkdownEditor";

export default {
  components: {
    MarkdownEditor
  },
  data: function() {
    return {
      submitting: false,
      note: {
        id: "",
        title: "",
        content: "",
        status: "",
        priority: 0,
        allowComment: false
      },
      rules: {
        title: [
          { required: true, message: "文章标题必须输入", trigger: "blur" }
        ],
        content: [
          { required: true, message: "文章内容不能为空", trigger: "blur" }
        ]
      }
    };
  },
  methods: {
    getNote() {
      const id = this.$route.params.id;
      if (id) {
        this.$api.auth.getNote(id).then(data => {
          this.initNote(data.data);
        });
      } else {
        this.note.id = "";
        this.note.title = "";
        this.note.content = "";
        this.note.status = this.$static.ArticleStatus.PUBLISH.key;
        this.priority = 0;
        this.allowComment = false;
      }
    },
    initNote(data) {
      this.note.id = data.id;
      this.note.title = data.title;
      this.note.content = data.content;
      this.note.status = data.status;
      this.note.priority = data.priority;
      this.note.allowComment = data.allowComment;
    },
    submitNote(formName, success) {
      if (this.submitting) {
        this.$util.message.warning("请不要提交过快!");
        return;
      }
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.submitting = true;
          this.$api.auth.saveNote(this.note).then(data => {
            if (data.success) {
              success(data.data);
            } else {
              this.$util.error("提交文章失败," + data.msg);
            }
            this.submitting = false;
          });
        }
      });
    },
    onPublish() {
      const _this = this;
      this.submitNote("noteForm", function() {
        _this.$util.message.success("发布页面成功!");
        _this.$router.push("/note");
      });
    },
    onSave() {
      const _this = this;
      this.submitNote("noteForm", function(data) {
        _this.$util.message.success("保存页面成功!");
        _this.$route.params.id = data;
        _this.getNote();
      });
    }
  },
  mounted() {
    this.getNote();
  }
};
</script>

<style scoped>
.el-select {
  width: 100%;
}
</style>
