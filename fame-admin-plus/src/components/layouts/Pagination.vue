<template>
  <div class="pagination">
    <el-pagination
      v-model:current-page="currentPageVal"
      v-model:page-size="pageSizeVal"
      :total="total"
      class="pagination"
      :page-sizes="[10, 20, 30, 40, 50, 100]"
      background
      small
      layout="total, sizes, prev, pager, next, jumper"
    />
  </div>
</template>

<script lang="ts">
  import { defineComponent, ref, watch } from 'vue'

  export default defineComponent({
    props: {
      currentPage: {
        type: Number,
        default: 1
      },
      pageSize: {
        type: Number,
        default: 10
      },
      total: {
        type: Number,
        default: 1
      }
    },
    emits: ['update:currentPage', 'update:pageSize'],
    setup(props, { emit }) {
      const currentPageVal = ref(props.currentPage)
      const pageSizeVal = ref(props.pageSize)

      watch(
        () => currentPageVal.value,
        (newVal) => emit('update:currentPage', newVal)
      )

      watch(
        () => pageSizeVal.value,
        (newVal) => emit('update:pageSize', newVal)
      )

      return {
        currentPageVal,
        pageSizeVal
      }
    }
  })
</script>

<style lang="scss" scoped>
  @import '~/assets/css/admin.scss';

  .pagination {
    @include layout(center);
    transform: translateY(30px);
    margin-bottom: 30px;
  }
</style>
