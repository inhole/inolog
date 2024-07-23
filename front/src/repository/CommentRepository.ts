import { inject, singleton } from 'tsyringe'
import HttpRepository from '@/repository/HttpRepository'
import type CommentWrite from '@/entity/comment/CommentWrite'
import Comment from '@/entity/comment/Comment'

@singleton()
export default class CommentRepository {
  constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {}

  public write(postId: number, request: CommentWrite) {
    return this.httpRepository.post({
      path: `/api/posts/${postId}/comments`,
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
}
