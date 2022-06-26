export interface RestResponse<T> {
  code: number
  success: string
  msg?: string
  data: T
}

export interface Page<T> {
  pageNum: number
  pageSize: number
  total: number
  pages: number
  orderBy: number
  list: Array<T>
}

export interface BreadCrumbItem {
  name?: string
  title?: string
  link?: string
  parent?: string
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
