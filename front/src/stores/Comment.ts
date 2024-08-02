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

export const useCommentStore = defineStore('comment', () => {
  const state = reactive({
    commentList: new Paging<Comment>(),
    commentWrite: new CommentWrite()
  })

  const router = useRouter()

  const COMMENT_REPOSITORY = container.resolve(CommentRepository)

  const write = (postId: number) => {
    router.push(`/comment/write/${postId}`)
  }

  function writeComment(postId: number) {
    COMMENT_REPOSITORY.write(postId, state.commentWrite)
      .then(() => {
        ElMessage({ type: `success`, message: '댓글 작성이 완료되었습니다.' })
        // getList(postId)
        router.back()
      })
      .catch((e: HttpError) => {
        ElMessage({ type: 'error', message: e.getMessage() })
      })
  }

  function getList(postId: number, page = 1) {
    COMMENT_REPOSITORY.getList(postId, page).then((commentList) => {
      state.commentList = commentList
    })
  }

  function deleteComment(comment: Comment, postId: number) {
    ElMessageBox.confirm('정말로 삭제하시겠습니까?', 'warning', {
      title: '삭제',
      confirmButtonText: '삭제',
      cancelButtonText: '취소',
      type: 'warning'
    }).then(() => {
      COMMENT_REPOSITORY.delete(comment)
        .then(() => {
          ElMessage({ type: `success`, message: '댓글 삭제가 완료되었습니다.' })
          getList(postId)
        })
        .catch((e: HttpError) => {
          ElMessage({ type: 'error', message: e.getMessage() })
        })
    })
  }

  return { state, write, writeComment, getList, deleteComment }
})
