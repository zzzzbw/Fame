import axios from 'axios'
import qs from 'qs'
import { Message, Loading } from 'element-ui'
import router from '../router/index'
import serverConfig from '../../server-config'

const Axios = axios.create({
  baseURL: serverConfig.api + 'api/', // 本地做反向代理
  timeout: 5000,
  responseType: 'json',
  withCredentials: true, // 是否允许带cookie这些
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'
  }
})

let loadingInstance = null
// 请求拦截（配置发送请求的信息） 传参序列化
Axios.interceptors.request.use((config) => {
  if (loadingInstance === null) {
    loadingInstance = Loading.service({target: '#main', fullscreen: false})
  }

  if (
    config.method === 'post' ||
    config.method === 'put' ||
    config.method === 'delete'
  ) {
    // 序列化
    config.data = qs.stringify(config.data)
  }
  return config
}, (error) => {
  Message({
    showClose: true,
    message: error,
    type: 'error.data.error.message'
  })
  return Promise.reject(error)
})

// 响应拦截（配置请求回来的信息）
Axios.interceptors.response.use(function (response) {
  // 处理响应数据
  if (loadingInstance !== null) {
    loadingInstance.close()
    loadingInstance = null
  }
  return response
}, function (error) {
  // 处理响应失败
  if (loadingInstance !== null) {
    loadingInstance.close()
    loadingInstance = null
  }
  let msg = '网络错误'
  if (!error.response) {
    msg = error.message
  } else {
    switch (error.response.status) {
      case 999:
        msg = '未登陆，请先登陆'
        router.push('/admin/login')
        break
      case 404:
        msg = '该页面不存在'
        router.push('/error/404/' + msg)
        break
      default:
        router.push('/error/' + error.response.status + '/' + msg)
        break
    }
  }
  Message({
    showClose: true,
    message: msg,
    type: 'error'
  })

  return Promise.reject(error)
})

/**
 * get 请求方法
 * @param url
 * @param params
 * @returns {Promise}
 */
export function get (url, params = {}) {
  return new Promise((resolve, reject) => {
    Axios.get(url, {
      params: params
    }).then(response => {
      resolve(response.data)
    }).catch(err => {
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
export function post (url, params = {}) {
  return new Promise((resolve, reject) => {
    Axios.post(url, params).then(response => {
      resolve(response.data)
    }, err => {
      reject(err)
    })
  })
}

/**
 * put方法
 * @param url
 * @param params
 * @returns {Promise}
 */
export function put (url, params = {}) {
  return new Promise((resolve, reject) => {
    Axios.put(url, params).then(response => {
      resolve(response.data)
    }, err => {
      reject(err)
    })
  })
}

/**
 * delete方法
 * @param url
 * @param params
 * @returns {Promise}
 */
export function del (url, params = {}) {
  return new Promise((resolve, reject) => {
    Axios.delete(url, {
      params: params
    }).then(response => {
      resolve(response.data)
    }).catch(err => {
      reject(err)
    })
  })
}
