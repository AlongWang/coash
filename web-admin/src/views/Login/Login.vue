<template>
  <div class="el-login h-[100%] relative lt-xl:bg-[var(--login-bg-color)] lt-sm:px-10px lt-xl:px-10px lt-md:px-10px">
    <div class="relative h-full flex mx-auto">
      <div class="el-login__left flex-1 bg-gray-500 bg-opacity-20 relative p-30px lt-xl:hidden">
      </div>
      <div class="flex-1 p-30px lt-sm:p-10px dark:bg-[var(--login-bg-color)] relative">
        <!-- 右上角的主题、语言选择 -->
        <div class="flex justify-between items-center text-white at-2xl:justify-end at-xl:justify-end">
          <div class="flex items-center at-2xl:hidden at-xl:hidden">
            <img alt="" class="w-48px h-48px mr-10px" src="../../assets/images/logo.png" />
            <span class="text-20px font-bold">Admin</span>
          </div>
        </div>
        <!-- 右边的登录界面 -->
        <div class="h-full flex items-center m-auto w-[100%] at-2xl:max-w-500px at-xl:max-w-500px at-md:max-w-500px at-lg:max-w-500px">
          <!-- 账号登录 -->
          <LoginForm class="p-20px h-auto m-auto lt-xl:(rounded-3xl light:bg-white)" />
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>

import LoginForm from '../../components/Login/LoginForm.vue'
</script>

<style lang="scss" scoped>
.el-login {
  overflow: auto;

  &__left {
    &::before {
      position: absolute;
      top: 0;
      left: 0;
      z-index: -1;
      width: 100%;
      height: 100%;
      background-image: url('../../assets/images/login-bg.jpg');
      background-position: bottom;
      background-repeat: no-repeat;
      background-size: cover;
      content: '';
    }
  }
}
</style>
