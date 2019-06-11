const isProd = process.env.NODE_ENV === 'production'

const devUrl = 'http://127.0.0.1:9090/'
const devProxy = {
  host: '127.0.0.1',
  port: 9090
}

const prodUrl = ''
const prodProxy = {
  protocol: 'http',
  host: 'fame-nginx',
  port: 80
}

const config = {
  isProd: isProd,
  api: isProd ? prodUrl : devUrl,
  baseProxy: isProd ? prodProxy : devProxy
}

console.log('----------------------serverConfig----------------------')
console.log(config)
console.log('----------------------serverConfig----------------------')

export default config
