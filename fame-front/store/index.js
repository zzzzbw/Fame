import api from '~/plugins/api'

export const actions = {
  // 前端设置
  async getOptions({ commit }) {
    const { data } = await api.getOptions()
    commit('option/SET_DATA', data)
  },
  // 文章详情
  async getPost({ commit }, id) {
    const { data } = await api.getPost(id)
    commit('post/SET_DETAIL', data)
  },
  // 文章列表
  async getPosts({ commit }, params) {
    const { data } = await api.getPosts(params.page, params.limit, params.sort)
    const list = {
      data: data.list,
      totalPage: data.pages,
      currentPage: data.pageNum || 0
    }
    commit('post/SET_LIST', list)
  },
  // 自定义页面目录
  async getNoteMenu({ commit }) {
    const { data } = await api.getNoteMenu()
    commit('note/SET_MENU', data)
  },
  // 自定义页面详情
  async getNote({ commit }, id) {
    const { data } = await api.getNote(id)
    commit('note/SET_DETAIL', data)
  },
  // 分类列表
  async getCategories({ commit }) {
    const { data } = await api.getCategories()
    commit('category/SET_DATA', data)
  },
  // 标签
  async getTags({ commit }) {
    const { data } = await api.getTags()
    commit('tag/SET_DATA', data)
  },
  // 归档
  async getArchive({ commit }) {
    const { data } = await api.getArchives()
    commit('archive/SET_DATA', data)
  },
  // 评论列表
  async getComments({ commit }, params) {
    const { data } = await api.getComment(
      params.articleId,
      params.page,
      params.limit
    )
    const pagination = {
      list: data.list,
      total: data.total
    }
    commit('comment/SET_LIST', pagination)
    return pagination
  },
  // 提交评论
  submitComment({ commit }, params) {
    return api.postComment(
      params.articleId,
      params.replyCommentId,
      params.content,
      params.name,
      params.email,
      params.website
    )
  },
  // 赞同评论
  async agreeComment({ commit }, commentId) {
    const res = await api.assessComment(commentId, 'AGREE')
    if (res && res.success) {
      commit('comment/AGREE_COMMENT', commentId)
    }
    return res
  },
  // 反对评论
  async disagreeComment({ commit }, commentId) {
    const res = await api.assessComment(commentId, 'DISAGREE')
    if (res && res.success) {
      commit('comment/DISAGREE_COMMENT', commentId)
    }
    return res
  },
  // 全局服务初始化
  async nuxtServerInit({ dispatch }) {
    await dispatch('getOptions')
    await dispatch('getNoteMenu')
  }
}
