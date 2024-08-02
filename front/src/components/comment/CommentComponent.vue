<script setup lang="ts">
import { Delete } from '@element-plus/icons-vue'
import Comment from '@/entity/comment/Comment'
import { useCommentStore } from '@/stores/Comment'

const props = defineProps<{
  comment: Comment
  postId: number
}>()

const store = useCommentStore()
</script>

<template>
  <div class="comment">
    <el-container>
      <el-aside width="200px">
        <div class="author">{{ props.comment.author }}</div>
        <div class="regDate">{{ props.comment.getDisplayRegDate() }}</div>
        <div class="password">
          <el-input
            type="password"
            size="small"
            v-model="props.comment.password"
            placeholder="비밀번호"
          />
        </div>
      </el-aside>
      <el-main>{{ props.comment.content }}</el-main>

      <div class="delete">
        <el-button
          size="small"
          type="danger"
          :icon="Delete"
          @click="store.deleteComment(comment, postId)"
          circle
        />
      </div>
    </el-container>
  </div>
</template>

<style scoped lang="scss">
.comment {
  margin: 0.5rem;
  width: 100%;

  .author {
    font-weight: 600;
    font-size: 1.2rem;
  }

  .regDate {
    margin-top: 5px;
    color: #797979;
    font-size: 0.8rem;
  }

  .password {
    margin-top: 1rem;
    margin-bottom: 1rem;
  }

  .content {
    margin-top: 0.8rem;
  }

  .delete {
  }
}
</style>
