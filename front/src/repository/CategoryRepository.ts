import { inject, singleton } from 'tsyringe'
import HttpRepository from '@/repository/HttpRepository'
import type CommentWrite from '@/entity/comment/CommentWrite'
import Comment from '@/entity/comment/Comment'
import type CommentDelete from '@/entity/comment/CommentDelete'
import type Category from '@/entity/category/Category'

@singleton()
export default class CategoryRepository {
  constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {}

  public write(request: Category) {
    return this.httpRepository.post({
      path: `/api/category`,
      body: request
    })
  }

  public getList(postId: number, page: Number) {
    return this.httpRepository.getList<Comment>(
      {
        path: `/api/posts/${postId}/comments?page=${page}&size=3`
      },
      Comment
    )
  }

  public delete(request: CommentDelete) {
    return this.httpRepository.post({
      path: `/api/comments/${request.id}/delete`,
      body: request
    })
  }
}
