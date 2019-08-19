import axios from 'axios'
import qs from 'qs'
import serverConfig from '../config/server-config'

const Axios = axios.create({
  baseURL: serverConfig.url + '/api/', // 本地做反向代理
  timeout: 5000,
  withCredentials: true, // 是否允许带cookie这些
  // 转换request参数，只有'PUT', 'POST', 'PATCH' and 'DELETE'方法才会生效
  transformRequest: [
    data => {
      // 序列化参数数组时不设置索引，否则tomcat8.5以上无法接收特殊字符
      return qs.stringify(data, { indices: false })
    }
  ],
  // 序列化params参数
  paramsSerializer: params => {
    // 序列化参数数组时不设置索引，否则tomcat8.5以上无法接收特殊字符
    return qs.stringify(params, { indices: false })
  },
  proxy: serverConfig.proxy
})

// 请求拦截（配置发送请求的信息） 传参序列化
Axios.interceptors.request.use(
  config => {
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截（配置请求回来的信息）
Axios.interceptors.response.use(
  function(response) {
    // 处理响应数据
    return response
  },
  function(error) {
    // 处理响应失败
    return Promise.reject(error)
  }
)

/**
 * get 请求方法
 * @param url
 * @param params
 * @returns {Promise}
 */
export function get(url, params = {}) {
  return new Promise((resolve, reject) => {
    Axios.get(url, {
      params: params
    })
      .then(response => {
        resolve(response.data)
      })
      .catch(err => {
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
    Axios.post(url, params).then(
      response => {
        resolve(response.data)
      },
      err => {
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
    Axios.put(url, params).then(
      response => {
        resolve(response.data)
      },
      err => {
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
    Axios.delete(url, {
      params: params
    })
      .then(response => {
        resolve(response.data)
      })
      .catch(err => {
        reject(err)
      })
  })
}
