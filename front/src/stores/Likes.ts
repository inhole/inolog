import { defineStore } from 'pinia'
import { reactive } from 'vue'
import { container } from 'tsyringe'
import { useRouter } from 'vue-router'
import Likes from '@/entity/likes/Likes'
import LikesRepository from '@/repository/LikesRepository'

export const useLikesStore = defineStore('likes', () => {
  const router = useRouter()
  const LIKES_REPOSITORY = container.resolve(LikesRepository)
  const state = reactive({
    likes: new Likes()
  })

  function insertLikes(postId: number) {
    LIKES_REPOSITORY.insert(postId).then(() => {
      getLikes(postId)
    })
  }

  function deleteLikes(postId: number) {
    LIKES_REPOSITORY.delete(postId).then(() => {
      getLikes(postId)
    })
  }

  // 좋아요 개수 및 유무
  function getLikes(postId: number) {
    LIKES_REPOSITORY.getLikes(postId).then((likes) => {
      state.likes = likes
    })
  }

  return { state, insertLikes, deleteLikes, getLikes }
})
