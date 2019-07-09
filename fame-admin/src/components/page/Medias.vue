<template>
  <div>
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/admin' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item>媒体库</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <!--      <el-button type="primary" icon="el-icon-edit" @click="showUploadDialog"
                          >上传
                        </el-button>-->
      <upload :afterUpload="afterUpload"></upload>
      <el-divider></el-divider>
      <div class="media-list">
        <el-row>
          <el-col
            class="block"
            :xs="24"
            :sm="12"
            :md="12"
            :lg="6"
            :xl="4"
            v-for="media in mediaDatas"
            :key="media.id"
          >
            <media-item :media="media" :after-delete="init"></media-item>
          </el-col>
        </el-row>
      </div>
    </el-card>
    <div class="admin-page">
      <el-pagination
        layout="total, prev, pager, next"
        @current-change="init"
        :current-page.sync="currentPage"
        :page-size="pageSize"
        :total="total"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
import Upload from "../common/Upload";
import MediaItem from "../common/MediaItem";

export default {
  components: {
    MediaItem,
    Upload
  },
  data: function() {
    return {
      mediaDatas: [],
      total: 0,
      pageSize: 10,
      currentPage: 1,
      uploadDialog: false
    };
  },
  methods: {
    init() {
      this.$api.auth.getMedias(12, this.currentPage).then(data => {
        this.mediaDatas = data.data.list;
        this.total = data.data.total;
        this.pageSize = data.data.pageSize;
        for (let media of this.mediaDatas) {
          if (media.thumbUrl && media.thumbUrl !== "") {
            media.showUrl = this.$util.getServerMediaUrl(media.thumbUrl);
          } else {
            media.showUrl = this.$util.getServerMediaUrl(media.url);
          }
        }
      });
    },
    showUploadDialog() {
      this.uploadDialog = true;
    },
    afterUpload(response) {
      if (response.success) {
        this.init();
      }
    }
  },
  mounted() {
    this.currentPage = Number(this.$route.query.page) || 1;
    this.init();
  }
};
</script>

<style scoped>
.el-table {
  border: 1px solid #e6ebf5;
}

.admin-page {
  margin-top: 30px;
  text-align: center;
}

.media-list .block {
  padding: 12px;
  text-align: center;
  border-left: 1px solid #eff2f6;
  border-right: 1px solid #eff2f6;
  display: inline-block;
  box-sizing: border-box;
  vertical-align: top;
}
</style>
