const isProd = process.env.NODE_ENV === 'production'
const localhost = 'http://127.0.0.1:9090/'
// build的时候传入参数或者使用localhost
const baseUrl = process.env.VUE_APP_API_URL || localhost
const api = isProd ? baseUrl : localhost
export default {
  isProd,
  api
}
