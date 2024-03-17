import formCreate from '@form-create/element-ui'
import install from '@form-create/element-ui/auto-import'
import { App } from 'vue'

export const setupFormCreate = (app: App<Element>) => {
  formCreate.use(install)
  app.use(formCreate)
}