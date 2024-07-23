<script setup lang="ts">
import Comment from '@/components/Comment.vue'
import { container } from 'tsyringe'
import CommentRepository from '@/repository/CommentRepository'
import { reactive } from 'vue'
import CommentWrite from '@/entity/comment/CommentWrite'
import { ElMessage } from 'element-plus'
import type HttpError from '@/http/HttpError'
import { useRouter } from 'vue-router'

const props = defineProps<{
  postId: number
}>()

const state = reactive({
  commentWrite: new CommentWrite()
})

const COMMENT_REPOSITORY = container.resolve(CommentRepository)

function write() {
  COMMENT_REPOSITORY.write(props.postId, state.commentWrite)
    .then(() => {
      ElMessage({ type: `success`, message: '댓글 작성이 완료되었습니다.' })
    })
    .catch((e: HttpError) => {
      ElMessage({ type: 'error', message: e.getMessage() })
    })
}
</script>

<template>
  <div class="totalCount">댓글 0개</div>

  <Comment />
  <Comment />
  <Comment />

  <el-form label-position="top">
    <el-form-item label="작성자" size="small">
      <el-input
        v-model="state.commentWrite.author"
        id="author"
        placeholder="밥돌맨"
        size="small"
        minlength="1"
        maxlength="8"
      ></el-input>
    </el-form-item>

    <el-form-item label="비밀번호" size="small">
      <el-input
        v-model="state.commentWrite.password"
        type="password"
        id="password"
        placeholder="비밀번호"
        size="small"
        minlength="6"
        maxlength="30"
      ></el-input>
    </el-form-item>

    <el-form-item label="내용" size="small">
      <el-input
        v-model="state.commentWrite.content"
        id="content"
        type="textarea"
        :rows="5"
        :autosize="{ minRows: 5, maxRows: 4 }"
        size="small"
        minlength="10"
        maxlength="1000"
      ></el-input>
    </el-form-item>

    <el-form-item>
      <el-button type="primary" class="button" @click="write()" size="small">등록하기</el-button>
    </el-form-item>
  </el-form>
</template>

<style scoped lang="scss">
.totalCount {
  margin-top: 1rem;
}
</style>
