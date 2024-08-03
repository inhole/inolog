import { inject, singleton } from 'tsyringe'
import HttpRepository from '@/repository/HttpRepository'
import type CommentDelete from '@/entity/comment/CommentDelete'
import Category from '@/entity/category/Category'

@singleton()
export default class CategoryRepository {
  constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {}

  public write(request: Category) {
    return this.httpRepository.post({
      path: `/api/category`,
      body: request
    })
  }

  public getBaseList() {
    return this.httpRepository.getBaseList<Category>(
      {
        path: `/api/category`
      },
      Category
    )
  }

  public delete(categoryId: number) {
    return this.httpRepository.delete({
      path: `/api/category/${categoryId}`
    })
  }
}
