import { get, post, del } from '@/plugins/http'

const auth = {
  login(user) {
    return post('/admin/login', user)
  },
  logout() {
    return post('/admin/logout')
  },
  getUser() {
    return get('/admin/user')
  },
  resetUser(username, email) {
    const params = {
      username: username,
      email: email,
    }
    return post('/admin/reset/user', params)
  },
  resetPassword(oldPassword, newPassword) {
    const params = {
      oldPassword: oldPassword,
      newPassword: newPassword,
    }
    return post('/admin/reset/password', params)
  },
  getOptions() {
    return get('/admin/option/all')
  },
  saveOptions(options) {
    return post('/admin/option/save', options)
  },
  countArticle() {
    return get('/admin/article/count')
  },
  pageArticle(page, query) {
    const params = query || {}
    params.page = page
    return get('/admin/article', params)
  },
  getArticle(id) {
    return get('/admin/article/' + id)
  },
  saveArticle(article) {
    return post('/admin/article', article)
  },
  deleteArticle(id) {
    return del('/admin/article/' + id)
  },
  pageComment(page) {
    const params = {
      page: page,
    }
    return get('/admin/comment', params)
  },
  getCommentDetail(id) {
    return get('/admin/comment/' + id)
  },
  deleteComment(id) {
    return del('/admin/comment/' + id)
  },
  countComment() {
    return get('/admin/comment/count')
  },
  getAllCategories() {
    return get('/admin/category')
  },
  getAllTags() {
    return get('/admin/tag')
  },
  saveOrUpdateCategory(id, parentId, name) {
    const params = {
      id: id,
      parentId: parentId,
      name: name,
    }
    return post('/admin/category', params)
  },
  saveOrUpdateTag(id, name) {
    const params = {
      id: id,
      name: name,
    }
    return post('/admin/tag', params)
  },
  deleteCategory(id) {
    return del('/admin/category/' + id)
  },
  deleteTag(id) {
    return del('/admin/tag/' + id)
  },
  pageNote(page) {
    const params = {
      page: page,
    }
    return get('/admin/note', params)
  },
  pageMedia(limit, page) {
    const params = {
      limit: limit,
      page: page - 1,
    }
    return get('/admin/media', params)
  },
  deleteMedia(id) {
    return del('/admin/media/' + id)
  },
}

export default {
  auth,
}
