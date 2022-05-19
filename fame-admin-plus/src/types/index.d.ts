export interface RestResponse<T> {
  code: number
  success: string
  msg?: string
  data: T
}

export interface Pagination<T> {
  pageNum: number
  pageSize: number
  total: number
  pages: number
  orderBy: number
  list: Array[T]
}

export interface MediaItem {
  id: number
  name: string
  url: string
  thumbUrl: string
  suffix: string
  showUrl: string
}
