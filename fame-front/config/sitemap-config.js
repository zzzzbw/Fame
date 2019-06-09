import axios from 'axios'
import serverConfig from './server-config'

const articlesUrl = serverConfig.api + '/api/article?page=1&limit=999'

const config = {
  routes() {
    return axios
      .get(articlesUrl)
      .then(res => res.data.data.list.map(article => '/article/' + article.id))
  }
}

export default {
  config
}
