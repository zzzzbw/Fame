import api from '~/plugins/api'

export const actions = {
  // 文章详情
  async getArticle ({commit}, id) {
    let {data} = await api.getArticle(id)
    commit('article/SET_DETAIL', data)
  },
  // 文章列表
  async getArticles ({commit}, page) {
    let {data} = await api.getArticles(page)
    let list = {
      data: data.list,
      totalPage: data.pages,
      currentPage: data.pageNum || 1
    }
    commit('article/SET_LIST', list)
  },
  // 分类列表
  async getCategories ({commit}) {
    let {data} = await api.getCategories()
    commit('category/SET_DATA', data)
  },
  // 标签
  async getTags ({commit}) {
    let {data} = await api.getTags()
    commit('tag/SET_DATA', data)
  },
  // 归档
  async getArchive ({commit}) {
    let {data} = await api.getArchives()
    commit('archive/SET_DATA', data)
  },
  // 自定义页面
  async getPage ({commit}, title) {
    let {data} = await api.getPage(title)
    commit('article/SET_PAGE', data)
  },
  // 评论列表
  async getComments ({commit}, params) {
    let {data} = await api.getComment(params.articleId, params.page, params.limit)
    let pagination = {
      list: data.list,
      total: data.total
    }
    commit('comment/SET_LIST', pagination)
    return pagination
  },
  // 提交评论
  async submitComment ({commit}, params) {
    return api.postComment(params.articleId, params.replyCommentId, params.content,
      params.name, params.email, params.website)
  },
  // 赞同评论
  async agreeComment ({commit}, commentId) {
    const res = await api.assessComment(commentId, 'agree')
    if (res && res.success) {
      commit('comment/AGREE_COMMENT', commentId)
    }
    return res
  },
  // 反对评论
  async disagreeComment ({commit}, commentId) {
    const res = await api.assessComment(commentId, 'disagree')
    if (res && res.success) {
      commit('comment/DISAGREE_COMMENT', commentId)
    }
    return res
  }
}
