<template>
  <div>
    <el-row :gutter="30">
      <el-col :xs="24" :sm="12" :md="12" :lg="12" style="margin-top: 30px">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>标签列表</span>
            </div>
          </template>
          <ul class="meta-list">
            <li v-for="tag in tagList" :key="tag.id">
              <span class="meta" @click="clickTag(tag.id, tag.name)">
                {{ tag.name }}
              </span>
              <span style="float: right; clear: both">
                <span class="meta-count">{{ tag.articleInfos.length }}</span>
                <el-popconfirm
                  title="确定删除该标签?"
                  confirm-button-text="确定"
                  cancel-button-text="取消"
                  :icon="InfoFilled"
                  icon-color="red"
                  @confirm="deleteTag(tag.id)"
                >
                  <template #reference>
                    <el-button size="small" type="danger">删除</el-button>
                  </template>
                </el-popconfirm>
              </span>
            </li>
          </ul>
          <div class="meta-edit">
            <el-input
              v-model.trim="editTag.name"
              placeholder="请输入标签名称"
              class="meta-input"
            ></el-input>
            <el-button type="success" size="small" @click="saveOrUpdateTag">保存标签</el-button>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="12" :lg="12" style="margin-top: 30px">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>分类列表</span>
            </div>
          </template>
          <ul class="meta-list">
            <li v-for="category in categoryList" :key="category.id">
              <span class="meta" @click="clickCategory(category.id, category.name)">
                {{ category.name }}
              </span>
              <span style="float: right; clear: both">
                <span class="meta-count">{{ category.articleInfos.length }}</span>
                <el-popconfirm
                  title="确定删除该分类?"
                  confirm-button-text="确定"
                  cancel-button-text="取消"
                  :icon="InfoFilled"
                  icon-color="red"
                  @confirm="deleteCategory(category.id)"
                >
                  <template #reference>
                    <el-button size="small" type="danger">删除</el-button>
                  </template>
                </el-popconfirm>
              </span>
            </li>
          </ul>
          <div class="meta-edit">
            <el-input
              v-model.trim="editCategory.name"
              placeholder="请输入分类名称"
              class="meta-input"
            ></el-input>
            <el-button type="success" size="small" @click="saveOrUpdateCategory"
              >保存分类
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
  import { reactive, onMounted } from 'vue'
  import { Api } from '~/api'
  import { RestResponse } from '~/types/common'
  import { handleRestResponse } from '~/utils'
  import { InfoFilled } from '@element-plus/icons-vue'
  import { ElMessage } from 'element-plus'
  import { EditMeta, Meta } from '~/types/meta'

  const tagList = reactive<Array<Meta>>([])
  const editTag = reactive<EditMeta>({ id: null, name: '' })
  const categoryList = reactive<Array<Meta>>([])
  const editCategory = reactive<EditMeta>({ id: null, name: '' })

  async function initTagData() {
    const resp = (await Api.getAllTags()) as RestResponse<Array<Meta>>
    handleRestResponse(resp, (list) => {
      tagList.splice(0)
      for (let key in list) {
        let tag = list[key]
        tagList.push(tag)
      }
    })
  }

  async function deleteTag(id: number) {
    const resp = (await Api.deleteTag(id)) as RestResponse<void>
    handleRestResponse(resp, () => {
      ElMessage.success('删除成功!')
      initTagData()
    })
  }

  async function saveOrUpdateTag() {
    if (editTag.name === '') {
      ElMessage.error('标签名称不能为空')
      return
    }
    const resp = (await Api.saveOrUpdateTag(editTag.id, editTag.name)) as RestResponse<void>
    handleRestResponse(resp, () => {
      ElMessage.success('编辑标签成功!')
      initTagData()
      editTag.id = null
      editTag.name = ''
    })
  }

  function clickTag(id: number, name: string) {
    editTag.id = id
    editTag.name = name
  }

  async function initCategoryData() {
    const resp = (await Api.getAllCategories()) as RestResponse<Array<Meta>>
    handleRestResponse(resp, (list) => {
      categoryList.splice(0)
      for (let key in list) {
        let category = list[key]
        categoryList.push(category)
      }
    })
  }

  async function deleteCategory(id: number) {
    const resp = (await Api.deleteCategory(id)) as RestResponse<void>
    handleRestResponse(resp, () => {
      ElMessage.success('删除成功!')
      initCategoryData()
    })
  }

  function clickCategory(id: number, name: string) {
    editCategory.id = id
    editCategory.name = name
  }

  async function saveOrUpdateCategory() {
    if (editCategory.name === '') {
      ElMessage.error('分类名称不能为空')
      return
    }
    const resp = (await Api.saveOrUpdateCategory(
      editCategory.id,
      null,
      editCategory.name
    )) as RestResponse<void>
    handleRestResponse(resp, () => {
      ElMessage.success('编辑分类成功!')
      initCategoryData()
      editCategory.id = null
      editCategory.name = ''
    })
  }

  onMounted(() => {
    initTagData()
    initCategoryData()
  })
</script>

<style lang="scss" scoped>
  @import '~/assets/css/admin.scss';

  .card-header {
    @include layout(space-between, center);
  }

  .meta-list {
    margin: 0 0 30px 0;
    padding: 0;
    list-style: none;
  }

  .meta-list li {
    @include layout(space-between, center);
  }

  .meta-list .meta {
    box-shadow: 0 0 3px rgba(14, 14, 14, 0.3);
    margin: 0.4rem;
    max-width: 350px;
    padding: 0.4rem 0.5rem;
    white-space: nowrap;
    cursor: pointer;
    font-size: 14px;
    font-weight: 600;
    color: #333;
    border: 1px solid #ffd740;
    background-color: #ffd740;
  }

  .meta-list .meta:hover {
    box-shadow: 0 2px 5px 0 rgba(0, 0, 0, 0.16), 0 2px 10px 0 rgba(0, 0, 0, 0.12);
    transition: all 0.2s;
  }

  .meta-list .meta:active {
    box-shadow: inset 0 3px 5px rgba(0, 0, 0, 0.125);
  }

  .meta-list .meta-count {
    display: inline-block;
    min-width: 10px;
    line-height: 12px;
    padding: 4px 7px;
    margin-right: 20px;
    font-size: 11px;
    font-weight: 700;
    color: #fff;
    text-align: center;
    white-space: nowrap;
    vertical-align: baseline;
    background-color: #f36a5a;
    border-radius: 10px;
  }

  .meta-edit {
    @include layout(space-between, center);
  }

  .meta-input {
    width: 200px;
    margin-left: 5px;
  }
</style>
