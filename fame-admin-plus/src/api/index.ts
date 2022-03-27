import { get, post } from './http'

const Api = {
  login(user: any) {
    return post('/admin/login', user)
  },
  logout() {
    return post('/admin/logout')
  },
  getUser() {
    return get('/admin/user')
  }
}

export { Api }
