import { defineStore } from 'pinia'
import cache from '../cache'
import { DicData, getAllDictData } from '../api/dict'

export const useDictStore = defineStore('dict', {
  state: () => ({
    dictMap: new Map<string, any>(),
    isSet: false
  }),
  getters: {
    getIsSet(): boolean {
      return this.isSet
    }
  },
  actions: {
    async setDicMap() {
      const dictMap = cache.getDictMap()
      if (dictMap) {
        this.dictMap = dictMap
        this.isSet = true
      } else {
        const res = await getAllDictData()
        const dictMap = new Map<string, any>()
        res.forEach((dictData: DicData) => {
          const value = dictMap.get(dictData.dictType)
          if (!value) {
            dictMap.set(dictData.dictType, [])
          }
          dictMap.get(dictData.dictType).push({
            value: dictData.value,
            label: dictData.label,
            colorType: dictData.colorType,
            cssClass: dictData.cssClass
          })
        })
        this.dictMap = dictMap
        this.isSet = true
        cache.setDictMap(dictMap)
      }
    },
    getDictByType(dictType: string): any {
      if (!this.isSet) {
        this.setDicMap()
      }
      return this.dictMap.get(dictType)
    }
  }
})