import Vue from 'vue'
import App from './App'
import router from './router'
import Api from './util/api'
import FameUtil from './util/fame'
import Moment from 'moment'
import hljs from './plugins/highlight' // 引入lib而不是整个文件
import axios from 'axios'
import './plugins/element.js'

// 引用highlight
Vue.directive('highlight', function (el) {
  let blocks = el.querySelectorAll('pre code')
  blocks.forEach((block) => {
    hljs.highlightBlock(block)
  })
})

// 时间过滤器
Vue.filter('time', function (data, fmt) {
  fmt = fmt || 'YYYY-MM-DD hh:mm'
  return Moment(data).format(fmt)
})

Vue.config.productionTip = false
Vue.prototype.$api = Api
Vue.prototype.$axios = axios
Vue.prototype.$util = FameUtil
Vue.prototype.$moment = Moment

new Vue({
  render: h => h(App),
  router
}).$mount('#app')
