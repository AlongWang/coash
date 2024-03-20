import serverAPI from '../index.ts'

export function getAllDictData(): Promise<DicData[]> {
  return serverAPI.get('/system/dict-data/list-all-simple')
}

export interface DicData {
  dictType: string
  value: string
  label: string
  colorType: string
  cssClass: string
}