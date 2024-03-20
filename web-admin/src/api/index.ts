import axios, { AxiosError, AxiosInstance } from 'axios'

const config = {
  baseUrl: import.meta.env.VITE_BASE_URL,
  timeout: 10000
}

const serverAPI: AxiosInstance = axios.create({
  baseURL: config.baseUrl,
  timeout: config.timeout
})

serverAPI.interceptors.request.use(function(config) {
    return config
  }, function(error) {
    return Promise.reject(error)
  }
)

serverAPI.interceptors.response.use(function(response) {
  return response
}, function(error: AxiosError) {
  return Promise.reject(error)
})

export default {
  get: async <T = any>(url: string, option?: any) => {
    const res = await serverAPI.get(url, option)
    return res.data as unknown as T
  },
  post: async <T = any>(url: string, data?: any, option?: any) => {
    const res = await serverAPI.post(url, data, option)
    return res.data as unknown as T
  },
  put: async <T = any>(url: string, data?: any, option?: any) => {
    const res = await serverAPI.put(url, data, option)
    return res.data as unknown as T
  },
  delete: async <T = any>(url: string, option: any) => {
    const res = await serverAPI.delete(url, option)
    return res.data as unknown as T
  }
}