const isProd = process.env.NODE_ENV === 'production'

const devUrl = 'http://localhost:9090/'
const prodUrl = '/'
const devFrontUrl = 'http://localhost:3000/'
const prodFrontUrl = '/'

const api = isProd ? prodUrl : devUrl
const frontUrl = isProd ? prodFrontUrl : devFrontUrl
export default {
  isProd,
  api,
  frontUrl,
}
