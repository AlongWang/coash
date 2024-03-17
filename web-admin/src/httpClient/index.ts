import axios, { AxiosInstance } from 'axios'

const config = {
  baseUrl: import.meta.env.VITE_BASE_URL,
  timeout: 10000
}

const axiosInstance: AxiosInstance = axios.create({
  baseURL: config.baseUrl,
  timeout: config.timeout
})

const httpClient = (option: any) => {
  const { url, method, params, data, headersType, responseType, ...config } = option
  return axiosInstance({
    url: url,
    methods: method,
    params: params,
    data: data,
    ...config,
    responseType: responseType,
    headers: { 'Content-Type': headersType || 'application/json' }
  })
}

export default {
  get: async <T = any>(option: any) => {
    const res = await httpClient({ method: 'GET', ...option })
    return res.data as unknown as T
  },
  post: async <T = any>(option: any) => {
    const res = await httpClient({ method: 'POST', ...option })
    return res.data as unknown as T
  },
  delete: async <T = any>(option: any) => {
    const res = await httpClient({ method: 'DELETE', ...option })
    return res.data as unknown as T
  },
  put: async <T = any>(option: any) => {
    const res = await httpClient({ method: 'PUT', ...option })
    return res.data as unknown as T
  }
}