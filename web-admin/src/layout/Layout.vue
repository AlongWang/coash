<script setup lang="ts">

</script>

<template>
  <section class='w-[100%] h-[100%] relative'>
    <div></div>
    <div>
      <Menu></Menu>
    </div>
    <div class='absolute top-0 h-[100%]' style="transition: all">
      <el-scrollbar>
        <div>
          <ToolHeader></ToolHeader>
        </div>
        <TagsView></TagsView>
        <AppView></AppView>
      </el-scrollbar>
    </div>

    <Backtop></Backtop>

    <Setting></Setting>
  </section>
</template>

<style scoped>

</style>