import { defineStore } from 'pinia'
import cache from '../cache'
import { Menu } from '../api/login'
import { RouteRecordRaw } from 'vue-router'

export interface PermissionInfo {
  routers: RouteRecordRaw[]
  addRouters: RouteRecordRaw[]
  menuTabRouters: RouteRecordRaw[]
}

export const usePermissionStore = defineStore('permission', {
  state: (): PermissionInfo => ({
    routers: [],
    addRouters: [],
    menuTabRouters: []
  }),
  getters: {
    getRouters(): any {
      return this.routers
    },
    getAddRouters(): any {
      return this.addRouters
    },
    getMenuTabRouters(): any {
      return this.menuTabRouters
    }
  },
  actions: {
    async generateRoutes() {
      return new Promise<void>(async (resolve) => {
        let res = cache.getRoutes() ? cache.getRoutes() : []
        const routes = generateRoute(res)
        this.addRouters = routes.push({
          path: '/:path(.*)*',
          redirect: '/404',
          name: '404Page',
          meta: {
            hidden: true,
            breadcrumb: false
          }
        })
        resolve()
      })
    }
  }
})

function toCamelCase(str: string, firstUpperCase: boolean): string {
  let result = str.replace(/[-_](.)/g, (_, group) => group.toUpperCase())

  if (firstUpperCase) {
    result = result.charAt(0).toUpperCase() + result.slice(1)
  }

  return result
}

function generateRoute(routes: Menu[]): any {
  for (const route of routes) {
    let data = {
      path: route.path,
      name: route.name,
      component: {},
      redirect: '',
      meta: {
        title: route.name,
        icon: route.icon,
        hidden: !route.visible,
        noCache: !route.keepAlive,
        alwaysShow: route.children && route.children.length === 1 && (route.alwaysShow !== undefined ? route.alwaysShow : true)
      }
    }
    if (!route.children && route.parentId == '0' && route.component) {
      data.component = () => import('../layout/Layout.vue')
      data.name = toCamelCase(route.path, true) + 'Parent'
      data.redirect = ''
      data.meta.alwaysShow = true
    } else {
      if (route.children) {

      } else {

      }
    }
  }
}


