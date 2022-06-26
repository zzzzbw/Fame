export interface BaseComment {
  id?: number
  name?: string
  content?: string
}

export interface CommentListItem extends BaseComment {
  email?: string
  created?: string
}

export interface CommentDetail extends BaseComment {
  email?: string
  created?: string
  website?: string
  agree?: number
  disagree?: number
  ip?: string
  agent?: string
  hasReplay?: boolean
  postUrl?: string
  replayName?: string
  replay?: string
  article?: {
    id?: number
    title?: string
  }
  parentComment?: CommentDetail
}
