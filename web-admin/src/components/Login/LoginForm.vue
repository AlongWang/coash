<template>
  <el-form v-show="getShow" ref="formLogin" :model="loginData.loginForm" :rules="LoginRules" class="login-form"
           label-position="top" label-width="120px" size="large">
    <el-row style="margin-right: -10px; margin-left: -10px">
      <el-col :span="24" style="padding-right: 10px; padding-left: 10px">
        <el-form-item>
          <h2 class="mb-3 text-2xl font-bold text-center xl:text-3xl enter-x xl:text-center">
            登陆
          </h2>
        </el-form-item>
      </el-col>
      <el-col :span="24" style="padding-right: 10px; padding-left: 10px">
        <el-form-item prop="username">
          <el-input v-model="loginData.loginForm.username" placeholder="请输入用户名" />
        </el-form-item>
      </el-col>
      <el-col :span="24" style="padding-right: 10px; padding-left: 10px">
        <el-form-item prop="password">
          <el-input v-model="loginData.loginForm.password" placeholder="请输入密码"
                    show-password type="password" @keyup.enter="handleLogin()" />
        </el-form-item>
      </el-col>
      <el-col :span="24" style="padding-right: 10px; padding-left: 10px">
        <el-form-item>
          <el-button class="w-[100%]" type="primary" @click="handleLogin()">
            登录
          </el-button>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>
<script lang="ts" setup>
import { ElLoading } from 'element-plus'
import { RouteLocationNormalizedLoaded, useRouter } from 'vue-router'
import { usePermissionStore } from '../../store/permissionStore.ts'
import { login } from '../../api/login'
import cache from '../../cache'


const formLogin = ref()
const { currentRoute, push } = useRouter()
const permissionStore = usePermissionStore()
const redirect = ref<string>('')
const loginLoading = ref(false)

const getShow = computed(() => unref(getLoginState) === 'LOGIN')

const LoginRules = {
  username: [{
    required: true,
    message: '请输入用户名'
  }],
  password: [{
    required: true,
    message: '请输入密码'
  }]
}
const loginData = reactive({
  isShowPassword: false,
  loginForm: {
    username: 'admin',
    password: 'admin123',
    rememberMe: false
  }
})

// 登录
const handleLogin = async () => {
  loginLoading.value = true
  try {
    const data = await validForm(formLogin)
    if (!data) {
      return
    }
    const res = await login(loginData.loginForm)
    if (!res) {
      return
    }
    ElLoading.service({
      lock: true,
      text: '正在加载系统中...',
      background: 'rgba(0, 0, 0, 0.7)'
    })
    cache.setToken(res)
    if (!redirect.value) {
      redirect.value = '/'
    }
    // 判断是否为SSO登录
    if (redirect.value.indexOf('sso') !== -1) {
      window.location.href = window.location.href.replace('/login?redirect=', '')
    } else {
      push({ path: redirect.value || permissionStore.addRouters[0].path })
    }
  } catch {
    loginLoading.value = false
  } finally {
    setTimeout(() => {
      const loadingInstance = ElLoading.service()
      loadingInstance.close()
    }, 400)
  }
}

watch(
  () => currentRoute.value,
  (route: RouteLocationNormalizedLoaded) => {
    redirect.value = route?.query?.redirect as string
  },
  {
    immediate: true
  }
)

const currentState = ref('LOGIN')

async function validForm<T extends Object = any>(formRef: Ref<any>) {
  const form = unref(formRef)
  if (!form) return
  const data = await form.validate()
  return data as T
}

const getLoginState = computed(() => currentState.value)

</script>

<style lang="scss" scoped>
:deep(.anticon) {
  &:hover {
    color: var(--el-color-primary) !important;
  }
}

.login-code {
  float: right;
  width: 100%;
  height: 38px;

  img {
    width: 100%;
    height: auto;
    max-width: 100px;
    vertical-align: middle;
    cursor: pointer;
  }
}
</style>
