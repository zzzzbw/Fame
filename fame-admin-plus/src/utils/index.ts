import { RestResponse } from '~/types/common'
import { FRONT_URL, SERVER_URL } from '~/config'
import { AxiosResponse } from 'axios'

export function handleRestResponse<T>(
  resp: RestResponse<T>,
  successHandle: (data: T) => void,
  errorHandle: Function = (resp: RestResponse<T>) => {
    console.log(resp)
  }
): void {
  if (!resp.success) {
    errorHandle(resp)
    return
  }
  successHandle(resp.data)
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

export function getConstValue(key: string, constArr: Array<any>, defaultVal = ''): string {
  for (let i = 0; i < constArr.length; i++) {
    const element = constArr[i]
    if (element.key == key) {
      return element.value
    }
  }
  return defaultVal
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

export function downloadFile(response: AxiosResponse) {
  const { data, headers } = response
  let fileName = '下载文件'
  const disposition = headers['content-disposition']
  const filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/
  const matches = filenameRegex.exec(disposition)
  if (matches != null && matches[1]) {
    fileName = matches[1].replace(/['"]/g, '')
  }
  const blob = new Blob([data], { type: headers['content-type'] })
  const dom = document.createElement('a')
  const url = window.URL.createObjectURL(blob)
  dom.href = url
  dom.download = decodeURI(fileName)
  dom.style.display = 'none'
  document.body.appendChild(dom)
  dom.click()
  dom.parentNode?.removeChild(dom)
  window.URL.revokeObjectURL(url)
}
