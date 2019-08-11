import { Message } from "element-ui";
import serverConfig from "../../server-config";
/************************************************************/
/**
 *  全局常量
 */
const STATIC = {
  ArticleStatus: {
    PUBLISH: {
      key: "PUBLISH",
      value: "公开"
    },
    DRAFT: {
      key: "DRAFT",
      value: "隐藏"
    },
    getValue: key => {
      switch (key) {
        case STATIC.ArticleStatus.PUBLISH.key:
          return STATIC.ArticleStatus.PUBLISH.value;
        case STATIC.ArticleStatus.DRAFT.key:
          return STATIC.ArticleStatus.DRAFT.value;
      }
    }
  },
  PostPriority: {
    NORMAL: {
      key: 0,
      value: "普通"
    },
    TOP: {
      key: 999,
      value: "置顶"
    },
    getValue: key => {
      switch (key) {
        case STATIC.PostPriority.NORMAL.key:
          return STATIC.PostPriority.NORMAL.value;
        case STATIC.PostPriority.TOP.key:
          return STATIC.PostPriority.TOP.value;
      }
    }
  }
};

/************************************************************/
/**
 * 通用工具类
 */
const FUNCTIONS = {
  /**
   * 获取服务器链接
   * @returns {string}
   */
  getServerUrl: () => {
    return serverConfig.api;
  },
  /**
   * 获取服务器媒体链接
   * @param url
   * @returns {string}
   */
  getServerMediaUrl: url => {
    return this.getServerUrl() + "media/" + url;
  },
  /**
   * 标签转字符串
   * @param tags
   * @returns {string}
   */
  tagsToString: tags => {
    let str = "";
    for (let i = 0; i < tags.length; i++) {
      str += tags[i] + ",";
    }
    return str.substr(0, str.length - 1);
  },
  /**
   * 字符串转标签
   * @param str
   * @returns {Array}
   */
  stringToTags: str => {
    if (str !== null && str !== "") {
      let tags;
      tags = str.split(",");
      return tags;
    } else {
      return [];
    }
  },
  /**
   * 复制文字到剪切板
   * @param text
   */
  copyText: text => {
    const oInput = document.createElement("input");
    oInput.value = text;
    document.body.appendChild(oInput);
    oInput.select(); // 选择对象
    document.execCommand("Copy"); // 执行浏览器复制命令
    oInput.className = "oInput";
    oInput.style.display = "none";
  },
  /**
   * 通用提示信息
   * @type {{success: message.success, warning: message.warning, error: message.error, info: message.info}}
   */
  message: {
    success: function(message) {
      Message({
        showClose: true,
        message: message || "成功",
        type: "success"
      });
    },
    warning: function(message) {
      Message({
        showClose: true,
        message: message || "警告",
        type: "warning"
      });
    },
    info: function(message) {
      Message({
        showClose: true,
        message: message || "提示"
      });
    },
    error: function(message) {
      Message({
        showClose: true,
        message: message || "异常",
        type: "error"
      });
    }
  }
};

export default {
  STATIC,
  FUNCTIONS
};
