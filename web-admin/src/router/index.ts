import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import { App } from 'vue'
import staticsRouter from './statics'
import NProgress from 'nprogress'
import cache from '../cache'
import { useUserStore } from '../store/userStore.ts'
import { usePermissionStore } from '../store/permissionStore.ts'

const router = createRouter({
  history: createWebHistory(),
  strict: true,
  routes: staticsRouter as RouteRecordRaw[],
  scrollBehavior: () => ({ left: 0, top: 0 })
})

export const setupRouter = (app: App<Element>) => {
  app.use(router)
}

router.beforeEach(async (to, _from, next) => {
  NProgress.start()
  //判断用户的登录状态
  if (cache.getAccessToken()) {
    //已登录
    if (to.path === '/login') {
      //如果是登录页面路径，就直接跳转到首页
      next({ path: '/' })
    } else {
      //获取用户信息
      const userStore = useUserStore()
      if (!userStore.getIsSet) {
        //如果用户信息未获取，初始化用户信息
        await userStore.setUserInfo()
        const permissionStore = usePermissionStore()
        await permissionStore.generateRoutes()
        for (const route of permissionStore.getAddRouters()) {
          router.addRoute(route)
        }
        next({ ...to, replace: true })
      } else {
        next()
      }
    }
  } else {
    //未登录
    if (to.path === '/login') {
      //如果是登录页面路径，就直接next()
      next()
    } else {
      //不然就跳转到登录；
      next(`/login?redirect=${to.fullPath}`)
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})

export default router