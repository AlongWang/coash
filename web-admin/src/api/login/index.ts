import httpClient from '../../httpClient'

export const getInfo = () => {
  return httpClient.get({ url: '/system/auth/get-permission-info' })
}