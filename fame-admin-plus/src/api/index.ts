import { get, post, del, download } from './http'

const Api = {
  login(user: any) {
    return post('/admin/login', user)
  },
  logout() {
    return post('/admin/logout')
  },
  getUser() {
    return get('/admin/user')
  },
  resetUser(username: string, email: string) {
    const params = {
      username: username,
      email: email
    }
    return post('/admin/reset/user', params)
  },
  resetPassword(oldPassword: string, newPassword: string) {
    const params = {
      oldPassword: oldPassword,
      newPassword: newPassword
    }
    return post('/admin/reset/password', params)
  },
  getOptions() {
    return get('/admin/option/all')
  },
  saveOptions(options: object) {
    return post('/admin/option/save', options)
  },
  countArticle() {
    return get('/admin/article/count')
  },
  pageArticle(page: number, query: object = {}) {
    const params = query || {}
    // TODO
    // params.pagea = page
    return get('/admin/article', params)
  },
  getArticle(id: number) {
    return get('/admin/article/' + id)
  },
  saveArticle(article: object) {
    return post('/admin/article', article)
  },
  deleteArticle(id: number) {
    return del('/admin/article/' + id)
  },
  importArticle(file: Blob, articleId: number) {
    const formData = new FormData()
    formData.append('file', file)
    return post('/admin/backup/import/' + articleId, formData)
  },
  exportArticle(articleId: number) {
    return download('/admin/backup/export/' + articleId)
  },
  pageComment(page: number) {
    const params = {
      page: page
    }
    return get('/admin/comment', params)
  },
  getCommentDetail(id: number) {
    return get('/admin/comment/' + id)
  },
  deleteComment(id: number) {
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
  saveOrUpdateCategory(id: number, parentId: number, name: string) {
    const params = {
      id: id,
      parentId: parentId,
      name: name
    }
    return post('/admin/category', params)
  },
  saveOrUpdateTag(id: number, name: string) {
    const params = {
      id: id,
      name: name
    }
    return post('/admin/tag', params)
  },
  deleteCategory(id: number) {
    return del('/admin/category/' + id)
  },
  deleteTag(id: number) {
    return del('/admin/tag/' + id)
  },
  pageNote(page: number) {
    const params = {
      page: page
    }
    return get('/admin/note', params)
  },
  pageMedia(limit: number, page: number) {
    const params = {
      limit: limit,
      page: page - 1
    }
    return get('/admin/media', params)
  },
  deleteMedia(id: number) {
    return del('/admin/media/' + id)
  }
}

export { Api }
