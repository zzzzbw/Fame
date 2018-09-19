const isProd = process.env.NODE_ENV === 'production'
const localhost = 'http://127.0.0.1:9090/'
// build的时候传入参数或者使用localhost
const baseUrl = process.env.BASE_URL || localhost
exports.isProd = isProd
exports.api = isProd ? baseUrl : localhost
