<template>
  <div>
    <media-upload :after-upload="afterUpload"></media-upload>
    <el-divider></el-divider>
    <div class="media-list">
      <el-row>
        <el-col
          v-for="media in mediaDatas"
          :key="media.id"
          class="block"
          :xs="24"
          :sm="12"
          :md="12"
          :lg="6"
          :xl="4"
        >
          <media-item :media="media" :after-delete="init"></media-item>
        </el-col>
      </el-row>
    </div>
    <pagination :page-size="pageSize" :total="total" @change-page="changePage"></pagination>
  </div>
</template>

<script></script>

<style scoped>
  .el-table {
    border: 1px solid #e6ebf5;
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
