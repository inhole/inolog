import HttpRepository from '@/repository/HttpRepository'
import { inject, singleton } from 'tsyringe'
import type PostWrite from '@/entity/post/PostWrite'
import Post from '@/entity/post/Post'
import type { UnwrapRef } from 'vue'

@singleton()
export default class PostRepository {
  constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {}

  public write(request: PostWrite) {
    return this.httpRepository.post({
      path: '/api/posts',
      body: request
    })
  }

  public get(postId: number) {
    return this.httpRepository.get<Post>({ path: `/api/posts/${postId}` }, Post)
  }

  public getList(page: Number) {
    return this.httpRepository.getList<Post>(
      {
        path: `/api/posts?page=${page}&size=3`
      },
      Post
    )
  }

  public delete(postId: number) {
    return this.httpRepository.delete({
      path: `/api/posts/${postId}`
    })
  }

  public edit(postId: number, request: Post | any) {
    return this.httpRepository.patch({
      path: `/api/posts/${postId}`,
      body: request
    })
  }
}
