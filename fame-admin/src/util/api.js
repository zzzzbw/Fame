import { get, post, del } from "../plugins/http";
import util from "./fame";

const auth = {
  login(user) {
    return post("/admin/login", user);
  },
  logout() {
    return post("/admin/logout");
  },
  getUser() {
    return get("/admin/user");
  },
  resetUser(username, email) {
    const params = {
      username: username,
      email: email
    };
    return post("/admin/reset/user", params);
  },
  resetPassword(oldPassword, newPassword) {
    const params = {
      oldPassword: oldPassword,
      newPassword: newPassword
    };
    return post("/admin/reset/password", params);
  },
  getOptions() {
    return get("/admin/option/all");
  },
  saveOptions(options) {
    return post("/admin/option/save", options);
  },
  getArticleCount() {
    return get("/admin/article/count");
  },
  getLogs(page) {
    const params = {
      page: page
    };
    return get("/admin/site/logs", params);
  },
  getArticles(page, title, status, category, tag) {
    const params = {
      page: page,
      title: title,
      status: status,
      category: category,
      tag: tag
    };
    return get("/admin/article", params);
  },
  getArticle(id) {
    return get("/admin/article/" + id);
  },
  saveArticle(article) {
    return post("/admin/article", article);
  },
  deleteArticle(id) {
    return del("/admin/article/" + id);
  },
  getComments(page) {
    const params = {
      page: page
    };
    return get("/admin/comment", params);
  },
  getCommentDetail(id) {
    return get("/admin/comment/" + id);
  },
  deleteComment(id) {
    return del("/admin/comment/" + id);
  },
  getCommentCount() {
    return get("/admin/comment/count");
  },
  getAllCategories() {
    const params = {
      type: util.STATIC.META_CATEGORY
    };
    return get("/admin/meta", params);
  },
  getAllTags() {
    const params = {
      type: util.STATIC.META_TAG
    };
    return get("/admin/meta", params);
  },
  saveCategory(name) {
    const params = {
      name: name,
      type: util.STATIC.META_CATEGORY
    };
    return post("/admin/meta", params);
  },
  saveTag(name) {
    const params = {
      name: name,
      type: util.STATIC.META_TAG
    };
    return post("/admin/meta", params);
  },
  updateCategory(id, name) {
    const params = {
      name: name,
      type: util.STATIC.META_CATEGORY
    };
    return post("/admin/meta/" + id, params);
  },
  updateTag(id, name) {
    const params = {
      name: name,
      type: util.STATIC.META_TAG
    };
    return post("/admin/meta/" + id, params);
  },
  deleteCategory(name) {
    const params = {
      name: name,
      type: util.STATIC.META_CATEGORY
    };
    return del("/admin/meta", params);
  },
  deleteTag(name) {
    const params = {
      name: name,
      type: util.STATIC.META_TAG
    };
    return del("/admin/meta", params);
  },
  getPages(page) {
    const params = {
      page: page
    };
    return get("/admin/page", params);
  },
  getPage(id) {
    return get("/admin/page/" + id);
  },
  savePage(page) {
    return post("/admin/page", page);
  },
  deletePage(id) {
    return del("/admin/page/" + id);
  }
};

export default {
  auth
};
