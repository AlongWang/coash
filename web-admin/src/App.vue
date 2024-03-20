<script setup lang="ts">
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
