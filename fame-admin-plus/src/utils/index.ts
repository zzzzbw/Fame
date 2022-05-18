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

export function getServerMediaUrl(url: string): string {
  return FRONT_URL + 'media/' + url
}

export function copyText(text: string): void {
  const oInput = document.createElement('input')
  oInput.value = text
  document.body.appendChild(oInput)
  oInput.select() // 选择对象
  document.execCommand('Copy') // 执行浏览器复制命令
  oInput.className = 'oInput'
  oInput.style.display = 'none'
}
