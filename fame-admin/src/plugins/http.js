import axios from "axios";
import qs from "qs";
import { Message, Loading } from "element-ui";
import router from "../router/index";
import serverConfig from "../../server-config";

const axiosJson = axios.create({
  baseURL: serverConfig.api + "api/", // 本地做反向代理
  timeout: 5000,
  withCredentials: true, // 是否允许带cookie这些
  // 转换request参数，只有'PUT', 'POST', 'PATCH' and 'DELETE'方法才会生效
  transformRequest: [
    data => {
      // 序列化参数数组时不设置索引，否则tomcat8.5以上无法接收特殊字符
      return qs.stringify(data, { indices: false });
    }
  ],
  // 序列化params参数
  paramsSerializer: params => {
    // 序列化参数数组时不设置索引，否则tomcat8.5以上无法接收特殊字符
    return qs.stringify(params, { indices: false });
  }
});

let loadingInstance = null;
let loginError = false;

// 请求拦截（配置请求信息）
const requestInterceptor = {
  before: config => {
    if (loadingInstance === null) {
      loadingInstance = Loading.service({ target: "#main", fullscreen: false });
    }

    return config;
  },
  error: error => {
    Message({
      showClose: true,
      message: error,
      type: "error.data.error.message"
    });
    return Promise.reject(error);
  }
};

// 响应拦截（配置请求回来的信息）
const responseInterceptor = {
  after: response => {
    // 处理响应数据
    if (loadingInstance !== null) {
      loadingInstance.close();
      loadingInstance = null;
    }

    if (response.data && !response.data.success) {
      let msg = null;
      switch (response.data.code) {
        case 999:
          router.push("/login");
          msg = "未登录,请先登录";
          if (!loginError) {
            loginError = true;
            Message({
              showClose: true,
              message: msg,
              type: "error",
              onClose: function() {
                loginError = false;
              }
            });
          }
          break;
        default:
          msg = response.data.msg || "系统错误";
          console.error(
            "Axios response error.Url: " +
              response.request.responseURL +
              ", code: " +
              response.data.code +
              ", msg: " +
              response.data.msg
          );
          Message({
            showClose: true,
            message: msg,
            type: "error"
          });
      }
    }
    return response;
  },
  error: error => {
    // 处理响应失败
    if (loadingInstance !== null) {
      loadingInstance.close();
      loadingInstance = null;
    }
    let msg = "网络错误";
    if (!error.response) {
      msg = error.message;
    } else {
      router.push("/error/" + error.response.status + "/" + msg);
    }
    Message({
      showClose: true,
      message: msg,
      type: "error"
    });

    return Promise.reject(error);
  }
};

// 配置axiosJson拦截器
axiosJson.interceptors.request.use(
  requestInterceptor.before,
  requestInterceptor.error
);

axiosJson.interceptors.response.use(
  responseInterceptor.after,
  responseInterceptor.error
);

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
        params: params
      })
      .then(response => {
        resolve(response.data);
      })
      .catch(err => {
        reject(err);
      });
  });
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
      response => {
        resolve(response.data);
      },
      err => {
        reject(err);
      }
    );
  });
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
      response => {
        resolve(response.data);
      },
      err => {
        reject(err);
      }
    );
  });
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
        params: params
      })
      .then(response => {
        resolve(response.data);
      })
      .catch(err => {
        reject(err);
      });
  });
}
