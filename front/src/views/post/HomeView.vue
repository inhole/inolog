<script setup lang="ts">
import { onMounted } from 'vue'
import PostComponent from '@/components/post/PostComponent.vue'
import { usePostStore } from '@/stores/Post'

const store = usePostStore()

onMounted(() => {
  if (store.state.postList.totalCount === 0) {
    store.getList()
  }
})
</script>

<template>
  <div class="content">
    <span class="totalCount">게시글 수: {{ store.state.postList.totalCount }}</span>
    <ul class="posts">
      <li v-for="post in store.state.postList.items" :key="post.id">
        <PostComponent :post="post" />
      </li>
    </ul>

    <div class="pagination-container">
      <el-pagination
        :background="true"
        layout="prev, pager, next"
        v-model:current-page="store.state.postList.page"
        :total="store.state.postList.totalCount"
        :default-page-size="3"
        @current-change="(page: number) => store.getList(page)"
      />
    </div>
  </div>
</template>

<style scoped lang="scss">
.content {
  padding: 0 1rem 0 1rem;
  margin-bottom: 2rem;

  .totalCount {
    font-size: 0.88rem;
  }

  .posts {
    list-style: none;
    padding: 0;

    li {
      margin-bottom: 2.4rem;

      &:last-child {
        margin-bottom: 0;
      }
    }
  }

  .pagination-container {
    position: fixed;
    bottom: 1rem;
    left: 0;
    width: 100%;
    display: flex;
    justify-content: center;
    background-color: white;
    padding: 10px 0;
    //box-shadow: 0 -2px 5px rgba(0, 0, 0, 0.1);
  }
}
</style>
