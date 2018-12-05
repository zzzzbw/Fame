const axios = require('axios')
const baseUrl = process.env.BASE_URL || 'http://127.0.0.1:3000'
const host = process.env.PROXY_HOST || '127.0.0.1:9090'
const articlesUrl = 'http://' + host + '/api/article?page=1&limit=999'

const config = {
  hostname: baseUrl,
  routes () {
    return axios.get(articlesUrl)
      .then(res => res.data.data.list.map(article => '/article/' + article.id))
  }
}
exports.config = config
