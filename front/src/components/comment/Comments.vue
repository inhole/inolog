<script setup lang="ts">
import CommentComponent from '@/components/comment/CommentComponent.vue'
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
  <div class="comments" v-if="store.state.commentList.totalCount > 0">
    <div class="title">
      <div>
        댓글 {{ store.state.commentList.totalCount }}개
        <el-button type="primary" class="button" @click="store.write(postId)" size="small" link
          >등록</el-button
        >
      </div>
    </div>

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
</template>

<style scoped lang="scss">
.title {
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
