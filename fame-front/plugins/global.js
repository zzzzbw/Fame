import Vue from 'vue'
import util from './tools'
import * as filters from './filters'
import clickOutside from './clickoutside'
import {highlight} from './highlight'

Vue.prototype.$util = util

// 引用 fiters
Object.keys(filters).forEach(key => Vue.filter(key, filters[key]))

// 引用clickOutside
Vue.directive('click-outside', clickOutside)
// 引用highlight
Vue.directive('highlight', highlight)
