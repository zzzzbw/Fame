<template>
  <div class="pagination">
    <el-pagination
      :page-size="pageSize"
      :total="total"
      :current-page="currentPageVal"
      class="pagination"
      :page-sizes="[10, 20, 30, 40, 50, 100]"
      background
      small
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script lang="ts">
  import { defineComponent, ref } from 'vue'

  export default defineComponent({
    props: {
      currentPage: {
        type: Number,
        default: 1
      },
      pageSize: {
        type: Number,
        default: 0
      },
      total: {
        type: Number,
        default: 1
      }
    },
    emits: ['handleCurrentChange'],
    setup(props, { emit }) {
      const currentPageVal = ref(props.currentPage)

      const handleCurrentChange = (newPage: number) => {
        currentPageVal.value = newPage
        emit('handleCurrentChange', currentPageVal.value)
      }

      const handleSizeChange = () => {}

      return {
        currentPageVal,
        handleCurrentChange,
        handleSizeChange
      }
    }
  })
</script>

<style lang="scss" scoped>
  @import '~/assets/css/admin.scss';

  .pagination {
    @include layout(center);
    transform: translateY(30px);
  }
</style>
