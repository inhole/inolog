import { inject, singleton } from 'tsyringe'
import HttpRepository from '@/repository/HttpRepository'
import type CommentWrite from '@/entity/comment/CommentWrite'

@singleton()
export default class CommentRepository {
  constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {}

  public write(postId: number, request: CommentWrite) {
    return this.httpRepository.post({
      path: `/api/posts/${postId}/comments`,
      body: request
    })
  }

  public getList() {}
}
