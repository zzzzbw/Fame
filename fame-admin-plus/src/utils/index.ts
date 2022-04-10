import { RestResponse } from '~/types'
import { FRONT_URL } from '~/config'

export function handleRestResponse<T>(resp: RestResponse<T>, handle: (data: T) => void): void {
  if (!resp.success) {
    console.log(resp)
    return
  }
  handle(resp.data)
}

export function setToken(token: string, refreshToken: string): void {
  localStorage.setItem('token', token)
  localStorage.setItem('refreshToken', refreshToken)
}

export function removeToken(): void {
  localStorage.removeItem('token')
  localStorage.removeItem('refreshToken')
}

export function getFrontArticleUrl(id: number): string {
  return FRONT_URL + 'article/' + id
}
