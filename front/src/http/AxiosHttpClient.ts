import axios, { type AxiosError, type AxiosInstance, type AxiosRequestConfig } from 'axios'
import { ElMessage } from 'element-plus'
import HttpError from '@/http/HttpError'

export default class AxiosHttpClient {
  private readonly client: AxiosInstance = axios.create({
    timeout: 3000,
    timeoutErrorMessage: '네트워크 상태를 확인해주세요. :('
  })

  public async request(config: AxiosRequestConfig) {
    return this.client.request(config).catch((e: AxiosError<any>) => {
      return Promise.reject(new HttpError(e))
    })
  }
}
