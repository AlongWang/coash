<script setup lang="ts">
import pageError from '../../assets/svgs/403.svg'
import networkError from '../../assets/svgs/500.svg'
import noPermission from '../../assets/svgs/403.svg'

interface ErrorMap {
  url: string
  message: string
}

const errorMap: {
  [key: string]: ErrorMap
} = {
  '404': {
    url: pageError,
    message: '抱歉，您访问的页面不存在。'
  },
  '500': {
    url: networkError,
    message: '抱歉，服务器报告错误。'
  },
  '403': {
    url: noPermission,
    message: `抱歉，您无权访问此页面。`
  }
}

const emit = defineEmits(['errorClick'])

const btnClick = () => {
  emit('errorClick', props.type)
}


const props = defineProps({
  type: String
})
</script>

<template>
  <div class="flex justify-center">
    <div class="text-center">
      <img :src="type && errorMap[type].url" alt="" width="350" />
      <div class="text-14px text-[var(--el-color-info)]">{{ type && errorMap[type].message }}</div>
      <div class="mt-20px">
        <el-button type="primary" @click="btnClick">返回首页</el-button>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>