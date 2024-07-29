<script setup lang="ts">
import CommentComponent from '@/components/CommentComponent.vue'
import { onMounted } from 'vue'
import { useCommentStore } from '@/stores/comment'

const props = defineProps<{
  postId: number
}>()

const store = useCommentStore()

onMounted(() => {
  store.getList(props.postId)
})
</script>

<template>
  <div class="comments" v-if="store.state.commentList.totalCount > 1">
    <div class="totalCount">댓글 {{ store.state.commentList.totalCount }}개</div>
    <ul class="deprecation-comment">
      <li v-for="comment in store.state.commentList.items" :key="comment.id">
        <CommentComponent :comment="comment" :postId="postId" />
      </li>
    </ul>

    <div class="pagination-container">
      <el-pagination
        :background="false"
        size="small"
        layout="prev, pager, next"
        v-model:current-page="store.state.commentList.page"
        :total="store.state.commentList.totalCount"
        :default-page-size="3"
        @current-change="(page: number) => store.getList(postId, page)"
      />
    </div>
  </div>

  <el-form label-position="top">
    <el-form-item label="작성자" size="small">
      <el-input
        v-model="store.state.commentWrite.author"
        id="author"
        placeholder="1~8글자로 입력해주세요."
        size="small"
        minlength="1"
        maxlength="8"
      ></el-input>
    </el-form-item>

    <el-form-item label="비밀번호" size="small">
      <el-input
        v-model="store.state.commentWrite.password"
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
        v-model="store.state.commentWrite.content"
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
      <el-button type="primary" class="button" @click="store.write(postId)" size="small"
        >등록하기</el-button
      >
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
