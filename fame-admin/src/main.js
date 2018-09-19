import Vue from 'vue'
import App from './App'
import router from './router'
import Api from './util/api'
import FameUtil from './util/fame'
import Moment from 'moment'
import hljs from './util/highlight' // 引入lib而不是整个文件
import axios from 'axios'

import {
  Pagination,
  Menu,
  Submenu,
  MenuItem,
  MenuItemGroup,
  Input,
  Checkbox,
  CheckboxButton,
  CheckboxGroup,
  Dialog,
  Select,
  Option,
  OptionGroup,
  Button,
  ButtonGroup,
  Table,
  TableColumn,
  Breadcrumb,
  BreadcrumbItem,
  Form,
  FormItem,
  Tag,
  Row,
  Col,
  Container,
  Loading,
  Message,
  MessageBox,
  Switch
} from 'element-ui'

Vue.use(Pagination)
Vue.use(Menu)
Vue.use(Submenu)
Vue.use(MenuItem)
Vue.use(MenuItemGroup)
Vue.use(Input)
Vue.use(Checkbox)
Vue.use(CheckboxButton)
Vue.use(CheckboxGroup)
Vue.use(Dialog)
Vue.use(Select)
Vue.use(Option)
Vue.use(OptionGroup)
Vue.use(Button)
Vue.use(ButtonGroup)
Vue.use(Table)
Vue.use(TableColumn)
Vue.use(Breadcrumb)
Vue.use(BreadcrumbItem)
Vue.use(Form)
Vue.use(FormItem)
Vue.use(Tag)
Vue.use(Row)
Vue.use(Col)
Vue.use(Container)
Vue.use(Switch)

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
Vue.prototype.$message = Message
Vue.prototype.$confirm = MessageBox.confirm
Vue.prototype.$loading = Loading

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  template: '<App/>',
  components: {App}
})
