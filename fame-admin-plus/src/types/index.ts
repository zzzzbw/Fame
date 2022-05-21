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
  list: Array<T>
}

export interface MediaItem {
  id: number
  name: string
  url: string
  thumbUrl: string
  suffix: string
  showUrl: string
}

export const ArticleStatus = [
  {
    key: 'PUBLISH',
    value: '公开'
  },
  {
    key: 'DRAFT',
    value: '隐藏'
  }
]

export const ArticlePriority = [
  {
    key: 0,
    value: '普通'
  },
  {
    key: '999',
    value: '置顶'
  }
]
