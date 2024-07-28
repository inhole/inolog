<script setup lang="ts">
import CommentComponent from '@/components/CommentComponent.vue'
import { container } from 'tsyringe'
import CommentRepository from '@/repository/CommentRepository'
import { onMounted, reactive } from 'vue'
import CommentWrite from '@/entity/comment/CommentWrite'
import Comment from '@/entity/comment/Comment'
import { ElMessage } from 'element-plus'
import type HttpError from '@/http/HttpError'
import { useRouter } from 'vue-router'
import Paging from '@/entity/data/Paging'

const props = defineProps<{
  postId: number
}>()

const state = reactive({
  commentList: new Paging<Comment>(),
  commentWrite: new CommentWrite()
})

const COMMENT_REPOSITORY = container.resolve(CommentRepository)

function write() {
  COMMENT_REPOSITORY.write(props.postId, state.commentWrite)
    .then(() => {
      ElMessage({ type: `success`, message: '댓글 작성이 완료되었습니다.' })
      getList()
    })
    .catch((e: HttpError) => {
      ElMessage({ type: 'error', message: e.getMessage() })
    })
}

function getList(page = 1) {
  COMMENT_REPOSITORY.getList(props.postId, page).then((commentList) => {
    console.log('>>>', props.postId, commentList)
    state.commentList = commentList
  })
}

onMounted(() => {
  getList()
})
</script>

<template>
  <div class="comments" v-if="state.commentList.totalCount > 1">
    <div class="totalCount">댓글 {{ state.commentList.totalCount }}개</div>
    <ul class="deprecation-comment">
      <li v-for="comment in state.commentList.items" :key="comment.id">
        <CommentComponent :comment="comment" />
      </li>
    </ul>

    <div class="pagination-container">
      <el-pagination
        :background="false"
        size="small"
        layout="prev, pager, next"
        v-model:current-page="state.commentList.page"
        :total="state.commentList.totalCount"
        :default-page-size="3"
        @current-change="(page: number) => getList(page)"
      />
    </div>
  </div>

  <el-form label-position="top">
    <el-form-item label="작성자" size="small">
      <el-input
        v-model="state.commentWrite.author"
        id="author"
        placeholder="1~8글자로 입력해주세요."
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
        placeholder="6~30글자로 입력해주세요."
        size="small"
        minlength="6"
        maxlength="30"
      ></el-input>
    </el-form-item>

    <el-form-item label="내용" size="small">
      <el-input
        v-model="state.commentWrite.content"
        id="content"
        placeholder="10~1000자까지 입력해주세요."
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

.deprecation-comment {
  list-style-type: none;
  padding-left: 0.5rem;
}

.pagination-container {
  display: flex;
  justify-content: center;
  padding: 10px 0;
}
</style>
