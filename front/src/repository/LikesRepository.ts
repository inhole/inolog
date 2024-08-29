import HttpRepository from '@/repository/HttpRepository'
import { inject, singleton } from 'tsyringe'
import Likes from '@/entity/likes/Likes'

@singleton()
export default class LikesRepository {
  constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {}

  public insert(postId: number) {
    return this.httpRepository.post({
      path: `/api/posts/likes/${postId}`
    })
  }

  public delete(postId: number) {
    return this.httpRepository.delete({
      path: `/api/posts/likes/${postId}`
    })
  }

  public getLikes(postId: number) {
    return this.httpRepository.get(
      {
        path: `/api/posts/likes/${postId}`
      },
      Likes
    )
  }
}
