import Vue from 'vue'
import App from './App'
import router from './router'
import Api from './util/api'
import FameUtil from './util/fame'
import dayjs from 'dayjs'
import axios from 'axios'
import './plugins/element.js'

// 时间过滤器
Vue.filter('time', function (data, fmt) {
  fmt = fmt || 'YYYY-MM-DD hh:mm'
  return dayjs(data).format(fmt)
})

Vue.config.productionTip = false
Vue.prototype.$api = Api
Vue.prototype.$axios = axios
Vue.prototype.$util = FameUtil
Vue.prototype.$dayjs = dayjs

new Vue({
  render: h => h(App),
  router
}).$mount('#app')
