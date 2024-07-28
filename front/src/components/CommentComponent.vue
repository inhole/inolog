<script setup lang="ts">
import { Delete } from '@element-plus/icons-vue'
import Comment from '@/entity/comment/Comment'
import { container } from 'tsyringe'
import CommentRepository from '@/repository/CommentRepository'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import type HttpError from '@/http/HttpError'

const props = defineProps<{
  comment: Comment
}>()

const router = useRouter()
const COMMENT_REPOSITORY = container.resolve(CommentRepository)

function deleteComment() {
  ElMessageBox.confirm('정말로 삭제하시겠습니까?', 'warning', {
    title: '삭제',
    confirmButtonText: '삭제',
    cancelButtonText: '취소',
    type: 'warning'
  }).then(() => {
    COMMENT_REPOSITORY.delete(props.comment)
      .then(() => {
        ElMessage({ type: `success`, message: '댓글 삭제가 완료되었습니다.' })
        router.back()
      })
      .catch((e: HttpError) => {
        ElMessage({ type: 'error', message: e.getMessage() })
      })
  })
}
</script>

<template>
  <div class="comment">
    <el-container>
      <el-aside width="200px">
        <div class="author">{{ props.comment.author }}</div>
        <div class="regDate">{{ props.comment.getDisplayRegDate() }}</div>
        <el-input
          type="password"
          size="small"
          v-model="props.comment.password"
          placeholder="비밀번호"
        />
      </el-aside>
      <el-main>{{ props.comment.content }}</el-main>

      <div class="delete">
        <el-button size="small" type="danger" :icon="Delete" @click="deleteComment()" circle />
      </div>
    </el-container>
  </div>
</template>

<style scoped lang="scss">
.comment {
  margin: 0.5rem;
  width: 100%;

  .header {
    display: flex;
    justify-content: space-between;
  }

  .section {
    display: flex;
    flex-direction: column;
  }

  .author {
    font-weight: 600;
    font-size: 1.2rem;
  }

  .regDate {
    margin-top: 5px;
    color: #797979;
    font-size: 0.8rem;
  }

  .content {
    margin-top: 0.8rem;
  }

  .delete {
  }
}
</style>
