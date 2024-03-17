import { createPinia } from 'pinia'
import { App } from 'vue'


const pinia = createPinia()

export const setupStore = (app: App<Element>) => {
  app.use(pinia)
}

export { pinia }