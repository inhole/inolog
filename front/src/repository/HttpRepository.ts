import AxiosHttpClient, { type HttpRequestConfig } from '@/http/AxiosHttpClient'
import { inject, singleton } from 'tsyringe'
import { Axios } from 'axios'

/**
 * axios HTTP 요청
 */
@singleton()
export default class HttpRepository {
  constructor(@inject(AxiosHttpClient) private readonly httpClient: AxiosHttpClient) {}

  public async get(config: HttpRequestConfig) {
    return this.httpClient.request({ ...config, method: 'GET' })
  }

  public async post(config: HttpRequestConfig) {
    return this.httpClient.request({ ...config, method: 'POST' })
  }

  public async patch(config: HttpRequestConfig) {
    return this.httpClient.request({ ...config, method: 'PATCH' })
  }

  public async delete(config: HttpRequestConfig) {
    return this.httpClient.request({ ...config, method: 'DELETE' })
  }
}
