import { defineStore } from 'pinia'

interface AppState {
  mobile: boolean,
  collapse: boolean,
  layout: string
}

export const useAppStore = defineStore('app', {
  state: (): AppState => {
    return {
      mobile: false,
      collapse: false,
      layout: 'classic'
    }
  },
  getters: {
    getMobile(): boolean {
      return this.mobile
    },
    getCollapse(): boolean {
      return this.collapse
    },
    getLayout(): string {
      return this.layout
    }
  },
  actions: {
    setMobile(mobile: boolean): void {
      this.mobile = mobile
    },
    setCollapse(collapse: boolean): void {
      this.collapse = collapse
    },
    setLayout(layout: string): void {
      this.layout = layout
    }
  }
})