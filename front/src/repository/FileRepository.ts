import HttpRepository from '@/repository/HttpRepository'
import { inject, singleton } from 'tsyringe'

@singleton()
export default class FileRepository {
  constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {}

  public upload(formData: FormData) {
    return this.httpRepository.post({
      path: `/api/upload`,
      body: formData
    })
  }
}
