import AxiosHttpClient, { type HttpRequestConfig } from '@/http/AxiosHttpClient'
import { inject, singleton } from 'tsyringe'
import { Axios } from 'axios'
import { plainToInstance } from 'class-transformer'
import Null from '@/entity/data/Null'
import Paging from '@/entity/data/Paging'

/**
 * 공통 axios HTTP 요청
 */
@singleton()
export default class HttpRepository {
  constructor(@inject(AxiosHttpClient) private readonly httpClient: AxiosHttpClient) {}

  public get<T>(config: HttpRequestConfig, clazz: new (...args: any[]) => T): Promise<T> {
    return this.httpClient
      .request({ ...config, method: 'GET' })
      .then((response) => plainToInstance(clazz, response))
  }

  public getList<T>(
    config: HttpRequestConfig,
    clazz: new (...args: any[]) => T
  ): Promise<Paging<T>> {
    return this.httpClient.request({ ...config, method: 'GET' }).then((response) => {
      const paging = plainToInstance<Paging<T>, any>(Paging, response)
      const items = plainToInstance<T, any[]>(clazz, response.items)
      paging.setItems(items)
      return paging
    })
  }

  public post<T>(config: HttpRequestConfig, clazz?: new (...args: any[]) => T): Promise<T | any> {
    return this.httpClient.request({ ...config, method: 'POST' }).then((response) => {
      if (clazz) {
        plainToInstance(clazz, response)
      } else {
        plainToInstance(Null, response)
      }
    })
  }

  public patch<T>(config: HttpRequestConfig, clazz?: new (...args: any[]) => T): Promise<T | any> {
    return this.httpClient.request({ ...config, method: 'PATCH' }).then((response) => {
      if (clazz) {
        plainToInstance(clazz, response)
      } else {
        plainToInstance(Null, response)
      }
    })
  }

  public delete<T>(config: HttpRequestConfig, clazz?: new (...args: any[]) => T): Promise<T | any> {
    return this.httpClient.request({ ...config, method: 'DELETE' }).then((response) => {
      if (clazz) {
        plainToInstance(clazz, response)
      } else {
        plainToInstance(Null, response)
      }
    })
  }
}
