import axios, { AxiosRequestConfig, AxiosResponse } from 'axios'
import qs from 'qs'
import { ElMessage, ElLoading } from 'element-plus'
import router from '~/router'
import { SERVER_URL } from '~/config'

const axiosJson = axios.create({
  baseURL: SERVER_URL + 'api/', // 本地做反向代理
  timeout: 5000,
  // 序列化params参数
  paramsSerializer: (params) => {
    // 序列化参数数组时不设置索引，否则tomcat8.5以上无法接收特殊字符
    return qs.stringify(params, { indices: false })
  }
})

let loadingInstance: any = null
let loginError = false

// 请求拦截（配置请求信息）
const requestInterceptor = {
  before: (config: AxiosRequestConfig) => {
    loadingInstance = ElLoading.service({
      target: '#main',
      lock: true,
      text: 'Loading',
      background: 'rgba(0, 0, 0, 0.7)'
    })

    // 验证头
    if (localStorage.token) {
      // @ts-ignore
      config.headers.Authorization = 'Bearer ' + localStorage.token
    }

    return config
  },
  error: (error: any) => {
    ElMessage.error({
      showClose: true,
      message: error
    })
    return Promise.reject(error)
  }
}

// 响应拦截（配置请求回来的信息）
const responseInterceptor = {
  after: async (response: AxiosResponse) => {
    // 处理响应数据
    loadingInstance?.close()

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
  error: (error: any) => {
    // 处理响应失败
    loadingInstance?.close()
    let msg = '网络错误'
    if (!error.response) {
      msg = error.message
    } else {
      router.push('/error/' + error.response.status + '/' + msg)
    }
    ElMessage.error({
      showClose: true,
      message: msg
    })

    return Promise.reject(error)
  }
}

// 配置axiosJson拦截器
axiosJson.interceptors.request.use(requestInterceptor.before, requestInterceptor.error)

axiosJson.interceptors.response.use(responseInterceptor.after, responseInterceptor.error)

/**
 * 处理业务异常
 * @param {Object} response
 * @returns response
 */
async function handleServiceError(response: AxiosResponse) {
  switch (response.data.code) {
    case 999:
      return handleNotLogin(response)
    case 998:
      return handleLoginExpire(response)
    default:
      return handleDefaultError(response)
  }
}

/**
 * 用户未登陆
 * @param {Object} response
 * @returns response
 */
function handleNotLogin(response: AxiosResponse) {
  router.push('/login')
  if (!loginError) {
    loginError = true
    ElMessage.error({
      showClose: true,
      message: '未登录,请先登录',
      onClose: function () {
        loginError = false
      }
    })
  }
  return response
}

/**
 * 登陆超时
 * @param {Object} response
 * @returns response
 */
async function handleLoginExpire(response: AxiosResponse) {
  const token = localStorage.getItem('token')
  const refreshToken = localStorage.getItem('refreshToken')
  console.log('refreshToken: ' + refreshToken)
  if (refreshToken) {
    // 如果有refresh_token，则请求获取新的 token
    try {
      const res = await axios({
        method: 'POST',
        url: SERVER_URL + 'api/admin/refresh',
        headers: {
          Authorization: `Bearer ${token}`
        },
        data: {
          refreshToken
        }
      })
      // 如果获取成功，则把新的 token 更新到容器中
      console.log('刷新 token  成功', res)
      if (res.data && res.data.success) {
        localStorage.setItem('token', res.data.data.token)
        localStorage.setItem('refreshToken', res.data.data.refreshToken)
        // 把之前失败的用户请求继续发出去
        return axiosJson(response.config)
      } else {
        await router.push('/login')
        if (!loginError) {
          loginError = true
          ElMessage.error({
            showClose: true,
            message: '登陆超时,请重新登陆',
            onClose: function () {
              loginError = false
            }
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
function handleDefaultError(response: AxiosResponse) {
  const msg = response.data.msg || '系统错误'
  console.error(
    'Axios response error.Url: ' +
      response.request.responseURL +
      ', code: ' +
      response.data.code +
      ', msg: ' +
      response.data.msg
  )
  ElMessage.error({
    showClose: true,
    message: msg
  })
  return response
}

/**
 * get 请求方法
 * @param url
 * @param params
 * @returns {Promise}
 */
export function get(url: string, params?: object) {
  return new Promise((resolve, reject) => {
    axiosJson
      .get(url, {
        params: params
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
export function post(url: string, params?: object) {
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
export function put(url: string, params?: object) {
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
export function del(url: string, params?: object) {
  return new Promise((resolve, reject) => {
    axiosJson
      .delete(url, {
        params: params
      })
      .then((response) => {
        resolve(response.data)
      })
      .catch((err) => {
        reject(err)
      })
  })
}

export function download(url: string, params?: object) {
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
