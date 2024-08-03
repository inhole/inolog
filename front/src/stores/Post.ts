import { defineStore } from 'pinia'
import { reactive } from 'vue'
import Paging from '@/entity/data/Paging'
import Post from '@/entity/post/Post'
import { container } from 'tsyringe'
import PostRepository from '@/repository/PostRepository'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import type HttpError from '@/http/HttpError'
import PostWrite from '@/entity/post/PostWrite'
import Category from '@/entity/category/Category'
import CategoryRepository from '@/repository/CategoryRepository'

export const usePostStore = defineStore('post', () => {
  // 상태 관리
  const state = reactive({
    // 글 리스트
    postList: new Paging<Post>(),
    // 글 상세, 글 수정
    post: new Post(),
    // 글 작성
    postWrite: new PostWrite()
    // 카테고리
  })

  const router = useRouter()

  const POST_REPOSITORY = container.resolve(PostRepository)

  // 글 리스트 조회
  function getList(page = 1) {
    POST_REPOSITORY.getList(page).then((postList) => {
      state.postList = postList
    })
  }

  // 글 상세, 수정 조회
  function getPost(postId: number) {
    POST_REPOSITORY.get(postId)
      .then((post: Post) => {
        state.post = post
      })
      .catch((e) => {
        console.log(e)
      })
  }

  // 글 작성
  function write() {
    POST_REPOSITORY.write(state.postWrite)
      .then(() => {
        ElMessage({ type: 'success', message: '글 등록이 완료되었습니다.' })
        router.replace('/')
      })
      .catch((e: HttpError) => {
        ElMessage({ type: 'error', message: e.getMessage() })
      })
  }

  // 글 수정
  function edit(postId: number) {
    router.push(`/edit/${postId}`)
  }

  // 글 삭제
  function remove(postId: number) {
    ElMessageBox.confirm('정말로 삭제하시겠습니까?', 'warning', {
      title: '삭제',
      confirmButtonText: '삭제',
      cancelButtonText: '취소',
      type: 'warning'
    }).then(() => {
      POST_REPOSITORY.delete(postId).then(() => {
        ElMessage({ type: 'success', message: '성공적으로 삭제되었습니다' })
        router.back()
      })
    })
  }

  // 글 수정
  function editPost(postId: number) {
    POST_REPOSITORY.edit(postId, state.post)
      .then(() => {
        ElMessage({ type: 'success', message: '글 수정이 완료되었습니다.' })
        router.replace({ name: 'home' })
      })
      .catch((e: HttpError) => {
        ElMessage({ type: 'error', message: e.getMessage() })
      })
  }

  return { state, getList, getPost, write, edit, remove, editPost }
})
