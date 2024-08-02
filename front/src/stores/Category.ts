import { defineStore } from 'pinia'
import { reactive } from 'vue'
import Paging from '@/entity/data/Paging'
import Comment from '@/entity/comment/Comment'
import CommentWrite from '@/entity/comment/CommentWrite'
import { container } from 'tsyringe'
import CommentRepository from '@/repository/CommentRepository'
import { ElMessage, ElMessageBox } from 'element-plus'
import type HttpError from '@/http/HttpError'
import { useRouter } from 'vue-router'
import CategoryRepository from '@/repository/CategoryRepository'
import Category from '@/entity/category/Category'

export const useCategoryStore = defineStore('comment', () => {
  const state = reactive({
    category: new Category()
  })

  const router = useRouter()

  const CATEGORY_REPOSITORY = container.resolve(CategoryRepository)

  function write() {
    CATEGORY_REPOSITORY.write(state.category)
      .then(() => {
        ElMessage({ type: `success`, message: '카테고리 작성이 완료되었습니다.' })
        router.back()
      })
      .catch((e: HttpError) => {
        ElMessage({ type: 'error', message: e.getMessage() })
      })
  }

  function getList(postId: number, page = 1) {}

  function deleteComment(comment: Comment, postId: number) {}

  return { state, write, getList, deleteComment }
})
