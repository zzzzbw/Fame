const axios = require('axios')
const baseUrl = process.env.BASE_URL || 'http://127.0.0.1:3000'
const host = process.env.PROXY_HOST || '127.0.0.1:9090'
const articlesUrl = 'http://' + host + '/api/article?page=1&limit=999'

const config = [
  // A default feed configuration object
  {
    path: '/feed.xml', // The route to your feed.
    async create(feed) {
      feed.options = {
        title: 'Fame Blog',
        link: baseUrl + '/feed.xml',
        description: 'A nuxt blog by Fame'
      }

      const res = await axios.get(articlesUrl)
      const articles = res.data.data.list
      articles.forEach(article => {
        feed.addItem({
          title: article.title,
          id: article.id,
          link: baseUrl + '/article/' + article.id,
          description: article.content,
          content: article.content
        })
      })

      feed.addCategory('Nuxt.js')
    }, // The create function (see below)
    cacheTime: 1000 * 60 * 15, // How long should the feed be cached
    type: 'rss2' // Can be: rss2, atom1, json1
  }
]

exports.config = config
