import { RestResponse } from '~/types'
import { FRONT_URL, SERVER_URL } from '~/config'

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
  return SERVER_URL + 'media/' + url
}

export function getServerUploadMediaUrl(): string {
  return SERVER_URL + 'api/admin/media/upload'
}

export function getConstValue(key: string, constArr: Array<any>): string {
  for (let i = 0; i < constArr.length; i++) {
    const element = constArr[length]
    if (element.key == key) {
      return element.value
    }
  }
  return ''
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
