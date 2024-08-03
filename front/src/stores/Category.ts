import { defineStore } from 'pinia'
import { reactive } from 'vue'
import Comment from '@/entity/comment/Comment'
import { container } from 'tsyringe'
import { ElMessage, ElMessageBox } from 'element-plus'
import type HttpError from '@/http/HttpError'
import { useRouter } from 'vue-router'
import CategoryRepository from '@/repository/CategoryRepository'
import Category from '@/entity/category/Category'
import CategoryWrite from '@/entity/category/CategoryWrite'

export const useCategoryStore = defineStore('category', () => {
  const state = reactive({
    categoryList: new Array<Category>(),
    categoryWrite: new CategoryWrite()
  })

  const router = useRouter()

  const CATEGORY_REPOSITORY = container.resolve(CategoryRepository)

  function write() {
    CATEGORY_REPOSITORY.write(state.categoryWrite)
      .then(() => {
        ElMessage({ type: `success`, message: '카테고리 작성이 완료되었습니다.' })
        getList()
      })
      .catch((e: HttpError) => {
        ElMessage({ type: 'error', message: e.getMessage() })
      })
  }

  function getList() {
    CATEGORY_REPOSITORY.getBaseList().then((response) => {
      state.categoryList = response
    })
  }

  function deleteCategory(categoryId: number) {
    ElMessageBox.confirm('정말로 삭제하시겠습니까?', 'warning', {
      title: '삭제',
      confirmButtonText: '삭제',
      cancelButtonText: '취소',
      type: 'warning'
    }).then(() => {
      CATEGORY_REPOSITORY.delete(categoryId)
        .then(() => {
          ElMessage({ type: `success`, message: '삭제가 완료되었습니다.' })
          getList()
        })
        .catch((e: HttpError) => {
          ElMessage({ type: 'error', message: e.getMessage() })
        })
    })
  }

  return { state, write, getList, deleteCategory }
})
