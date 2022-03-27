const IS_PRD = process.env.NODE_ENV === 'production'

const devServerUrl = 'http://localhost:9090/'
const prdServerUrl = '/'
const devFrontUrl = 'http://localhost:3000/'
const prdFrontUrl = '/'

const SERVER_URL = IS_PRD ? prdServerUrl : devServerUrl
const FRONT_URL = IS_PRD ? prdFrontUrl : devFrontUrl
export { IS_PRD, SERVER_URL, FRONT_URL }
