import { defineStore } from 'pinia'
import cache from '../cache'
import { getPermissionInfo, loginOut } from '../api/login'

interface UserInfo {
  permissions: string[],
  roles: string[],
  isSet: boolean,
  user: User
}

interface User {
  id: string,
  nickname: string
}

export const useUserStore = defineStore('user', {
  state: (): UserInfo => ({
    permissions: [],
    roles: [],
    isSet: false,
    user: {
      id: '',
      nickname: ''
    }
  }),
  getters: {
    getPermissions(): string[] {
      return this.permissions
    },
    getRoles(): string[] {
      return this.roles
    },
    getIsSet(): boolean {
      return this.isSet
    },
    getUser(): any {
      return this.user
    }
  },
  actions: {
    async setUserInfo() {
      if (!cache.getAccessToken()) {
        this.resetUserState()
      } else {
        let permissionInfo = cache.getPermissionInfo() ? cache.getPermissionInfo() : await getPermissionInfo()
        this.permissions = permissionInfo.permissions
        this.roles = permissionInfo.roles
        this.user = permissionInfo.user
        this.isSet = true
        cache.setPermissionInfo(permissionInfo)
        cache.setRoutes(permissionInfo.menus)
      }
    },
    async loginOut() {
      await loginOut()
      cache.clear()
      this.resetUserState()
    },
    resetUserState() {
      this.permissions = []
      this.roles = []
      this.isSet = false
      this.user = {
        id: '',
        nickname: ''
      }
    }
  }
})