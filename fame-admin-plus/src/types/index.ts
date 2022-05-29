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

export interface EditMeta {
  id: number | null
  name: string
}

export interface Meta {
  id: number
  name: string
  articleInfos: []
}

export enum ArticleStatusEnum {
  PUBLISH = 'PUBLISH',
  DRAFT = 'DRAFT'
}

export const ArticleStatus = [
  {
    key: ArticleStatusEnum.PUBLISH,
    value: '公开'
  },
  {
    key: ArticleStatusEnum.DRAFT,
    value: '隐藏'
  }
]

export enum ArticlePriorityEnum {
  NORMAL = '0',
  TOP = '999'
}

export const ArticlePriority = [
  {
    key: ArticlePriorityEnum.NORMAL,
    value: '普通'
  },
  {
    key: ArticlePriorityEnum.TOP,
    value: '置顶'
  }
]
