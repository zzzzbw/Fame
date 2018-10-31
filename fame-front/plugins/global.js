import Vue from 'vue'
import util from './tools'
import * as filters from './filters'
import clickOutside from './clickoutside'
import { highlight } from './highlight'
import gravarar from 'gravatar'

Vue.prototype.$util = util
Vue.prototype.$gravarar = gravarar

// 引用 fiters
Object.keys(filters).forEach(key => Vue.filter(key, filters[key]))

// 引用clickOutside
Vue.directive('click-outside', clickOutside)
// 引用highlight
Vue.directive('highlight', highlight)
