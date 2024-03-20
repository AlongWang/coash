import serverAPI from '../index.ts'

export function getPermissionInfo(): Promise<PermissionInfo> {
  return serverAPI.get('/system/auth/get-permission-info')
}

export function loginOut(): Promise<boolean> {
  return serverAPI.post('/system/auth/logout')
}

export function login(data: UserLogin): Promise<TokenInfo> {
  return serverAPI.post('/system/auth/login', data)
}

export interface TokenInfo {
  userId: string,
  accessToken: string,
  refreshToken: string,
  expiresTime: number
}

export interface UserLogin {
  username: string,
  password: string
}

export interface PermissionInfo {
  permissions: string[]
  roles: string[]
  user: User
  menus: Menu[]
}

export interface User {
  id: string
  nickname: string
}

export interface Menu {
  id: string
  parentId: string
  path: string
  name: string
  component: string
  componentName: string
  icon: string
  visible: string
  keepAlive: string
  alwaysShow: string
  children: Menu[]
}