<template>
  <Error type="403" @error-click="push('/')" />
</template>
<script lang="ts" setup>
import { useRouter } from 'vue-router'
import Error from '../../components/Error/Error.vue'

const { push } = useRouter()
</script>
