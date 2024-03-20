import { RouteRecordRaw } from 'vue-router'

const staticsRouter: RouteRecordRaw[] = [
  {
    path: '/login',
    component: () => import('../views/Login/Login.vue'),
    name: 'Login',
    meta: {
      hidden: true,
      title: '登录',
      noTagsView: true
    }
  },
  {
    path: '/403',
    component: () => import('../views/Error/403.vue'),
    name: 'NoAccess',
    meta: {
      hidden: true,
      title: '403',
      noTagsView: true
    }
  },
  {
    path: '/404',
    component: () => import('../views/Error/404.vue'),
    name: 'NoFound',
    meta: {
      hidden: true,
      title: '404',
      noTagsView: true
    }
  },
  {
    path: '/500',
    component: () => import('../views/Error/500.vue'),
    name: 'Error',
    meta: {
      hidden: true,
      title: '500',
      noTagsView: true
    }
  },
]

export default staticsRouter