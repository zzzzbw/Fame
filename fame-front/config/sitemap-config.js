import api from '../plugins/api'

const config = {
  routes() {
    return api
      .getArticles(1, 999)
      .then(res => res.data.list.map(article => '/article/' + article.id))
  }
}

export default {
  config
}
