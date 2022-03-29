export function setToken(token: string, refreshToken: string): void {
  localStorage.setItem('token', token)
  localStorage.setItem('refreshToken', refreshToken)
}

export function removeToken(): void {
  localStorage.removeItem('token')
  localStorage.removeItem('refreshToken')
}
