export interface RestResponse<T> {
  code: number
  success: string
  msg?: string
  data: T
}
