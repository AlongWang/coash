import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import { setupStore } from './store'
import { setupFormCreate } from './plugins/formCreate'
import router, { setupRouter } from './router'

const app = createApp(App)

setupStore(app)
setupFormCreate(app)
setupRouter(app)

await router.isReady()

app.mount('#app')
