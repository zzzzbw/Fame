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

<script setup lang="ts">
  import { defineEmits, ref, watch } from 'vue'

  const props = defineProps({
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
  })

  const emit = defineEmits(['update:currentPage', 'update:pageSize'])

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
</script>

<style lang="scss" scoped>
  @import '~/assets/css/admin.scss';

  .pagination {
    @include layout(center);
    transform: translateY(30px);
    margin-bottom: 30px;
  }
</style>
