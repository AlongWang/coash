import WebStorageCache from 'web-storage-cache'
import { Menu, PermissionInfo, TokenInfo } from '../api/login'

const wsCache: WebStorageCache = new WebStorageCache({
  storage: 'localStorage'
})

export default {
  clear: () => {
    wsCache.clear()
  },
  set: (key: string, value: any) => {
    wsCache.set(key, value)
  },
  get: (key: string) => {
    return wsCache.get(key)
  },
  delete: (key: string) => {
    wsCache.delete(key)
  },
  setToken: (token: TokenInfo) => {
    wsCache.set('accessToken', token.accessToken)
    wsCache.set('refreshToken', token.refreshToken)
  },
  removeToken: () => {
    wsCache.delete('accessToken')
    wsCache.delete('refreshToken')
  },
  getAccessToken: () => {
    return wsCache.get('accessToken')
  },
  getRefreshToken: () => {
    return wsCache.get('refreshToken')
  },
  setPermissionInfo: (value: PermissionInfo) => {
    wsCache.set('permission', value)
  },
  getPermissionInfo: (): PermissionInfo => {
    return wsCache.get('permission')
  },
  setRoutes: (value: Menu[]) => {
    wsCache.set('routes', value)
  },
  getRoutes: (): Menu[] => {
    return wsCache.get('routes')
  },
  getDictMap: () => {
    return wsCache.get('dictMap')
  },
  setDictMap: (value: any) => {
    wsCache.set('dictMap', value)
  }
}