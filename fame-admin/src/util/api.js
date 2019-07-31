import { get, post, del } from "../plugins/http";

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
  countPost() {
    return get("/admin/post/count");
  },
  pagePost(page, query) {
    const params = query || {};
    params.page = page - 1;
    return get("/admin/post", params);
  },
  getPost(id) {
    return get("/admin/post/" + id);
  },
  savePost(article) {
    return post("/admin/post", article);
  },
  deletePost(id) {
    return del("/admin/article/" + id);
  },
  pageComment(page) {
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
  countComment() {
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
  pageNote(page) {
    const params = {
      page: page - 1
    };
    return get("/admin/note", params);
  },
  getNote(id) {
    return get("/admin/note/" + id);
  },
  saveNote(note) {
    return post("/admin/note", note);
  },
  deleteNote(id) {
    return del("/admin/note/" + id);
  },
  pageMedia(limit, page) {
    const params = {
      limit: limit,
      page: page - 1
    };
    return get("/admin/media", params);
  },
  deleteMedia(id) {
    return del("/admin/media/" + id);
  }
};

export default {
  auth
};
