import axios from 'axios'
import qs from 'qs'
import { Message, Loading } from 'element-ui'
import router from '../router/index'
import serverConfig from '../../server-config'

const axiosJson = axios.create({
  baseURL: serverConfig.api + 'api/', // 本地做反向代理
  timeout: 5000,
  // 序列化params参数
  paramsSerializer: (params) => {
    // 序列化参数数组时不设置索引，否则tomcat8.5以上无法接收特殊字符
    return qs.stringify(params, { indices: false })
  },
})

let loadingInstance = null
let loginError = false

// 请求拦截（配置请求信息）
const requestInterceptor = {
  before: (config) => {
    if (loadingInstance === null) {
      loadingInstance = Loading.service({ target: '#main', fullscreen: false })
    }

    // 验证头
    if (localStorage.token) {
      config.headers.Authorization = 'Bearer ' + localStorage.token
    }

    return config
  },
  error: (error) => {
    Message({
      showClose: true,
      message: error,
      type: 'error.data.error.message',
    })
    return Promise.reject(error)
  },
}

// 响应拦截（配置请求回来的信息）
const responseInterceptor = {
  after: async (response) => {
    // 处理响应数据
    if (loadingInstance !== null) {
      loadingInstance.close()
      loadingInstance = null
    }

    if (
      response.headers['content-type'] &&
      response.headers['content-type'].indexOf('json') === -1
    ) {
      // 不是json格式直接返回数据
      return response
    }

    if (response.data && !response.data.success) {
      return await handleServiceError(response)
    }
    return response
  },
  error: (error) => {
    // 处理响应失败
    if (loadingInstance !== null) {
      loadingInstance.close()
      loadingInstance = null
    }
    let msg = '网络错误'
    if (!error.response) {
      msg = error.message
    } else {
      router.push('/error/' + error.response.status + '/' + msg)
    }
    Message({
      showClose: true,
      message: msg,
      type: 'error',
    })

    return Promise.reject(error)
  },
}

// 配置axiosJson拦截器
axiosJson.interceptors.request.use(
  requestInterceptor.before,
  requestInterceptor.error
)

axiosJson.interceptors.response.use(
  responseInterceptor.after,
  responseInterceptor.error
)

/**
 * 处理业务异常
 * @param {Object} response
 * @returns response
 */
async function handleServiceError(response) {
  switch (response.data.code) {
    case 999:
      return handleNotLogin(response)
    case 998:
      return handleLoginExpire(response)
    default:
      return handleDefatutError(response)
  }
  return response
}

/**
 * 用户未登陆
 * @param {Object} response
 * @returns response
 */
function handleNotLogin(response) {
  router.push(
    '/login',
    () => {},
    () => {}
  )
  if (!loginError) {
    loginError = true
    Message({
      showClose: true,
      message: '未登录,请先登录',
      type: 'error',
      onClose: function () {
        loginError = false
      },
    })
  }
  return response
}

/**
 * 登陆超时
 * @param {Object} response
 * @returns response
 */
async function handleLoginExpire(response) {
  const token = localStorage.getItem('token')
  const refreshToken = localStorage.getItem('refreshToken')
  console.log('refreshToken: ' + refreshToken)
  if (refreshToken) {
    // 如果有refresh_token，则请求获取新的 token
    try {
      const res = await axios({
        method: 'POST',
        url: serverConfig.api + 'api/admin/refresh',
        headers: {
          Authorization: `Bearer ${token}`,
        },
        data: {
          refreshToken,
        },
      })
      // 如果获取成功，则把新的 token 更新到容器中
      console.log('刷新 token  成功', res)
      if (res.data && res.data.success) {
        localStorage.setItem('token', res.data.data.token)
        localStorage.setItem('refreshToken', res.data.data.refreshToken)
        // 把之前失败的用户请求继续发出去
        return axiosJson(response.config)
      } else {
        router.push(
          '/login',
          () => {},
          () => {}
        )
        if (!loginError) {
          loginError = true
          Message({
            showClose: true,
            message: '登陆超时,请重新登陆',
            type: 'error',
            onClose: function () {
              loginError = false
            },
          })
        }
      }
    } catch (err) {
      console.log('请求刷新 token 失败', err)
    }
  }
  return response
}

/**
 * 默认处理
 * @param {Object} response
 * @returns response
 */
function handleDefatutError(response) {
  let msg = response.data.msg || '系统错误'
  console.error(
    'Axios response error.Url: ' +
      response.request.responseURL +
      ', code: ' +
      response.data.code +
      ', msg: ' +
      response.data.msg
  )
  Message({
    showClose: true,
    message: msg,
    type: 'error',
  })
  return response
}

/**
 * get 请求方法
 * @param url
 * @param params
 * @returns {Promise}
 */
export function get(url, params = {}) {
  return new Promise((resolve, reject) => {
    axiosJson
      .get(url, {
        params: params,
      })
      .then((response) => {
        resolve(response.data)
      })
      .catch((err) => {
        reject(err)
      })
  })
}

/**
 * post 请求方法
 * @param url
 * @param params
 * @returns {Promise}
 */
export function post(url, params = {}) {
  return new Promise((resolve, reject) => {
    axiosJson.post(url, params).then(
      (response) => {
        resolve(response.data)
      },
      (err) => {
        reject(err)
      }
    )
  })
}

/**
 * put方法
 * @param url
 * @param params
 * @returns {Promise}
 */
export function put(url, params = {}) {
  return new Promise((resolve, reject) => {
    axiosJson.put(url, params).then(
      (response) => {
        resolve(response.data)
      },
      (err) => {
        reject(err)
      }
    )
  })
}

/**
 * delete方法
 * @param url
 * @param params
 * @returns {Promise}
 */
export function del(url, params = {}) {
  return new Promise((resolve, reject) => {
    axiosJson
      .delete(url, {
        params: params,
      })
      .then((response) => {
        resolve(response.data)
      })
      .catch((err) => {
        reject(err)
      })
  })
}

export function download(url, params = {}) {
  return new Promise((resolve, reject) => {
    axiosJson.post(url, params, { responseType: 'blob' }).then(
      (response) => {
        resolve(response)
      },
      (err) => {
        reject(err)
      }
    )
  })
}
