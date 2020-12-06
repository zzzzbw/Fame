<template>
  <div class="admin-page">
    <el-pagination
      background
      layout="prev, pager, next"
      @current-change="changePage"
      :current-page.sync="currentPage"
      :page-size="pageSize"
      :total="total"
    >
    </el-pagination>
  </div>
</template>

<script>
export default {
  name: 'Pagination',
  props: {
    pageSize: {
      type: Number,
      default: 0,
    },
    total: {
      type: Number,
      default: 1,
    },
  },
  data() {
    return { currentPage: 1 }
  },
  methods: {
    changePage() {
      this.$emit('change-page', this.currentPage)
    },
  },
  created() {},
}
</script>

<style scoped>
.admin-page {
  margin-top: 30px;
  text-align: center;
}
</style>
