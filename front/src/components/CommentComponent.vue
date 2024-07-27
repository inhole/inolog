<script setup lang="ts">
import { Delete } from '@element-plus/icons-vue'
import type Comment from '@/entity/comment/Comment'

const props = defineProps<{
  comment: Comment
}>()
</script>

<template>
  <div class="comment">
    <el-container>
      <el-aside width="200px">
        <div class="author">{{ props.comment.author }}</div>
        <div class="regDate">{{ props.comment.getDisplayRegDate() }}</div>
      </el-aside>
      <el-main>{{ props.comment.content }}</el-main>

      <div class="delete">
        <el-button size="small" type="danger" :icon="Delete" circle />
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
