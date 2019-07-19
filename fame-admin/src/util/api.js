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
  getArticles(page, title, status, category, tag) {
    const params = {
      page: page - 1,
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
      page: page - 1
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
    return get("/admin/category");
  },
  getAllTags() {
    return get("/admin/tag");
  },
  saveCategory(name) {
    const params = {
      name: name
    };
    return post("/admin/category", params);
  },
  saveTag(name) {
    const params = {
      name: name
    };
    return post("/admin/tag", params);
  },
  updateCategory(id, name) {
    const params = {
      name: name
    };
    return post("/admin/category/" + id, params);
  },
  updateTag(id, name) {
    const params = {
      name: name
    };
    return post("/admin/tag/" + id, params);
  },
  deleteCategory(name) {
    const params = {
      name: name
    };
    return del("/admin/category", params);
  },
  deleteTag(name) {
    const params = {
      name: name
    };
    return del("/admin/tag", params);
  },
  getPages(page) {
    const params = {
      page: page - 1
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
  },
  getMedias(limit, page) {
    const params = {
      limit: limit,
      page: page - 1
    };
    return get("/admin/media", params);
  },
  getMedia(id) {
    return get("/admin/media/" + id);
  },
  deleteMedia(id) {
    return del("/admin/media/" + id);
  }
};

export default {
  auth
};
