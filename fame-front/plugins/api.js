import { get, post } from './http'

const api = {
  getPosts(page, limit, sort) {
    const params = {
      page: page,
      limit: limit || 5,
      sort: sort
    }
    return get('/post', params)
  },
  getPost(id) {
    return get('/post/' + id)
  },
  getCategories() {
    return get('/category')
  },
  getTags() {
    return get('/tag')
  },
  getNoteMenu() {
    return get('/note')
  },
  getNote(id) {
    return get('/note/' + id)
  },
  getArchives() {
    return get('/archive')
  },
  getComment(articleId, page, limit) {
    const params = {
      articleId: articleId,
      page: page,
      limit: limit || 5
    }
    return get('comment', params)
  },
  postComment(articleId, parentId, content, name, email, website) {
    const params = {
      articleId: articleId,
      parentId: parentId,
      content: content,
      name: name,
      email: email,
      website: website
    }
    return post('/comment', params)
  },
  assessComment(commentId, assess) {
    const params = {
      assess: assess
    }
    return post('/comment/' + commentId + '/assess', params)
  },
  getOptions() {
    return get('/option')
  }
}

export default api
