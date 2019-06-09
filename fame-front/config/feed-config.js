import axios from 'axios'
import serverConfig from './server-config'
import defaultConfig from './default-config'
import tools from '../plugins/tools'

const articlesUrl = serverConfig.api + '/api/article?page=1&limit=999'
const optionsUrl = serverConfig.api + '/api/option'

const config = [
  // A default feed configuration object
  {
    path: '/feed.xml', // The route to your feed.
    async create(feed) {
      const optionsResp = await axios.get(optionsUrl)
      if (optionsResp.status !== 200 || !optionsResp.data.success) {
        console.error(
          'feed optionsResp error! status: ' +
            optionsResp.status +
            ', data: ' +
            optionsResp.data
        )
        return
      }
      const options = optionsResp.data.data
      feed.options = {
        title: options.meta_title || defaultConfig.meta_title,
        link: tools.formatWebsite(options.blog_website) + '/feed.xml',
        description: options.meta_description || defaultConfig.meta_description
      }

      const articleResp = await axios.get(articlesUrl)
      if (articleResp.status !== 200 || !articleResp.data.success) {
        console.error(
          'feed articleResp error! status: ' +
            articleResp.status +
            ', data: ' +
            articleResp.data
        )
        return
      }
      const articles = articleResp.data.data.list
      articles.forEach(article => {
        feed.addItem({
          title: article.title,
          id: article.id,
          link:
            tools.formatWebsite(options.blog_website) +
            '/article/' +
            article.id,
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

export default {
  config
}
