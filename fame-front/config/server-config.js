const isProd = process.env.NODE_ENV === 'production'
const localhost = 'http://127.0.0.1:9090/'
const localProxy = {
  host: '127.0.0.1',
  port: 9090
}
// 使用环境参数或者使用localhost
const baseUrl = process.env.baseUrl || localhost
const baseProxy = {
  protocol: 'http',
  host: process.env.proxyHost || localProxy.host,
  port: process.env.proxyPort || localProxy.port
}
exports.isProd = isProd
exports.api = isProd ? baseUrl : localhost
exports.baseProxy = isProd ? baseProxy : localProxy
