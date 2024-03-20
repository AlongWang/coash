import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import UnoCss from 'unocss/vite'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

// https://vitejs.dev/config/
export default defineConfig({
  server: {
    host: '0.0.0.0',
    port: 5173,
    open: true
  },
  plugins: [
    vue(),
    UnoCss(),
    AutoImport({
      resolvers: [ElementPlusResolver({ importStyle: true })],
      imports: ['vue'],
      dts: 'src/auto-imports.d.ts'
    }),
    Components({
      resolvers: [ElementPlusResolver({ importStyle: true })]
    })
  ]
})
