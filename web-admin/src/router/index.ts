import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import { App } from 'vue'
import staticsRouter from './statics'

const router = createRouter({
  history: createWebHistory(),
  strict: true,
  routes: staticsRouter as RouteRecordRaw[],
  scrollBehavior: () => ({ left: 0, top: 0 })
})

export const setupRouter = (app: App<Element>) => {
  app.use(router)
}

export default router