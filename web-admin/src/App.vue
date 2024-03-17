<script setup lang="ts">
import { watch } from 'vue'
import { useWindowSize } from '@vueuse/core'
import { useAppStore } from './store/appStore.ts'

const setCss = (css: string, val: any) => {
  document.documentElement.style.setProperty(css, val)
}

// 监听窗口变化
const appStore = useAppStore()
const { width } = useWindowSize()
watch(
  () => width.value,
  (width: number) => {
    if (width < 768) {
      !appStore.getMobile ? appStore.setMobile(true) : undefined
      setCss('--left-menu-min-width', '0')
      appStore.setCollapse(true)
      appStore.getLayout !== 'classic' ? appStore.setLayout('classic') : undefined
    } else {
      appStore.getMobile ? appStore.setMobile(false) : undefined
      setCss('--left-menu-min-width', '64px')
    }
  },
  {
    immediate: true
  }
)
</script>

<template>
  <el-config-provider
    :message="{ max: 1 }"
    :size="'default'">
    <router-view />
  </el-config-provider>
</template>

<style scoped>
</style>
