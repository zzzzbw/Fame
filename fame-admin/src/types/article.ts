import { Meta } from '~/types/meta'

export interface BaseArticle {
  id?: number
  title?: string
}

export interface Article extends BaseArticle {
  tagIds?: Array<number>
  categoryId?: number
  content?: string
  listShow?: boolean
  headerShow?: boolean
  status?: string
  priority?: number
  allowComment?: boolean
  publishTime?: number
}

export interface ArticleResp extends BaseArticle {
  tags?: Array<Meta>
  category?: { id?: number }
  content?: string
  listShow?: boolean
  headerShow: boolean
  status?: string
  priority?: number
  allowComment?: boolean
  publishTime?: number
}

export interface ArticleListItem extends BaseArticle {
  frontUrl?: string
  publishTime?: string
  category?: { name?: string }
  listShow?: string
  headerShow?: string
  status?: string
  priority?: string
}
