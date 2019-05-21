import { get, post, del } from "../plugins/http";
import util from "./fame";

const auth = {
  login(user) {
    return post("/admin/login", user);
  },
  logout() {
    return post("/admin/logout");
  },
  getUsername() {
    return get("/admin/username");
  },
  resetPassword(oldUsername, newUsername, oldPassword, newPassword) {
    let params = {
      oldUsername: oldUsername,
      newUsername: newUsername,
      oldPassword: oldPassword,
      newPassword: newPassword
    };
    return post("/admin/reset", params);
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
    let params = {
      page: page
    };
    return get("/admin/site/logs", params);
  },
  getArticles(page, title, status, category, tag) {
    let params = {
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
    let params = {
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
    let params = {
      type: util.STATIC.META_CATEGORY
    };
    return get("/admin/meta", params);
  },
  getAllTags() {
    let params = {
      type: util.STATIC.META_TAG
    };
    return get("/admin/meta", params);
  },
  saveCategory(name) {
    let params = {
      name: name,
      type: util.STATIC.META_CATEGORY
    };
    return post("/admin/meta", params);
  },
  saveTag(name) {
    let params = {
      name: name,
      type: util.STATIC.META_TAG
    };
    return post("/admin/meta", params);
  },
  updateCategory(id, name) {
    let params = {
      name: name,
      type: util.STATIC.META_CATEGORY
    };
    return post("/admin/meta/" + id, params);
  },
  updateTag(id, name) {
    let params = {
      name: name,
      type: util.STATIC.META_TAG
    };
    return post("/admin/meta/" + id, params);
  },
  deleteCategory(name) {
    let params = {
      name: name,
      type: util.STATIC.META_CATEGORY
    };
    return del("/admin/meta", params);
  },
  deleteTag(name) {
    let params = {
      name: name,
      type: util.STATIC.META_TAG
    };
    return del("/admin/meta", params);
  },
  getPages(page) {
    let params = {
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
