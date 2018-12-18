import {get, post, del} from '../plugins/http'
import util from './fame'

const auth = {
  login (user) {
    return post('/admin/login', user)
  },
  logout () {
    return post('/admin/logout')
  },
  getUsername () {
    return get('/admin/username')
  },
  resetPassword (username, oldPassword, newPassword) {
    let params = {
      username: username,
      oldPassword: oldPassword,
      newPassword: newPassword
    }
    return post('/admin/reset', params)
  },
  getSiteConfig () {
    return get('/admin/config/site')
  },
  saveSiteConfig (title, description, keywords, emailSend, emailUsername, emailPassword, emailHost, emailPort) {
    let params = {
      title: title,
      description: description,
      keywords: keywords,
      emailSend: emailSend,
      emailUsername: emailUsername,
      emailPassword: emailPassword,
      emailHost: emailHost,
      emailPort: emailPort
    }
    return post('/admin/config/site', params)
  },
  getArticleCount () {
    return get('/admin/article/count')
  },
  getLogs (page) {
    let params = {
      page: page
    }
    return get('/admin/site/logs', params)
  },
  getArticles (page) {
    let params = {
      page: page
    }
    return get('/admin/article', params)
  },
  getArticle (id) {
    return get('/admin/article/' + id)
  },
  saveArticle (article) {
    return post('/admin/article', article)
  },
  deleteArticle (id) {
    return del('/admin/article/' + id)
  },
  getComments (page) {
    let params = {
      page: page
    }
    return get('/admin/comment', params)
  },
  getCommentDetail (id) {
    return get('/admin/comment/' + id)
  },
  deleteComment (id) {
    return del('/admin/comment/' + id)
  },
  getCommentCount () {
    return get('/admin/comment/count')
  },
  getAllCategories () {
    let params = {
      type: util.STATIC.META_CATEGORY
    }
    return get('/admin/meta', params)
  },
  getAllTags () {
    let params = {
      type: util.STATIC.META_TAG
    }
    return get('/admin/meta', params)
  },
  saveCategory (name) {
    let params = {
      name: name,
      type: util.STATIC.META_CATEGORY
    }
    return post('/admin/meta', params)
  },
  saveTag (name) {
    let params = {
      name: name,
      type: util.STATIC.META_TAG
    }
    return post('/admin/meta', params)
  },
  updateCategory (id, name) {
    let params = {
      name: name,
      type: util.STATIC.META_CATEGORY
    }
    return post('/admin/meta/' + id, params)
  },
  updateTag (id, name) {
    let params = {
      name: name,
      type: util.STATIC.META_TAG
    }
    return post('/admin/meta/' + id, params)
  },
  deleteCategory (name) {
    let params = {
      name: name,
      type: util.STATIC.META_CATEGORY
    }
    return del('/admin/meta', params)
  },
  deleteTag (name) {
    let params = {
      name: name,
      type: util.STATIC.META_TAG
    }
    return del('/admin/meta', params)
  },
  getPages (page) {
    let params = {
      page: page
    }
    return get('/admin/page', params)
  },
  getPage (id) {
    return get('/admin/page/' + id)
  },
  savePage (page) {
    return post('/admin/page', page)
  },
  deletePage (id) {
    return del('/admin/page/' + id)
  }
}

export default {
  auth
}
