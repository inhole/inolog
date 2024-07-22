<script setup lang="ts">
import { onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'
import PostComponent from '@/components/PostComponent.vue'
import { container } from 'tsyringe'
import PostRepository from '@/repository/PostRepository'
import Paging from '@/entity/data/Paging'
import type Post from '@/entity/post/Post'

const POST_REPOSITORY = container.resolve(PostRepository)

const router = useRouter()

type StateType = {
  postList: Paging<Post>
}

const state = reactive<StateType>({
  postList: new Paging<Post>()
})

function getList(page = 1) {
  POST_REPOSITORY.getList(page).then((postList) => {
    console.log('>>>', postList)
    state.postList = postList
  })
}

onMounted(() => {
  getList()
})
</script>

<template>
  <div class="content">
    <span class="totalCount">게시글 수: {{ state.postList.totalCount }}</span>
    <ul class="posts">
      <li v-for="post in state.postList.items" :key="post.id">
        <PostComponent :post="post" />
      </li>
    </ul>

    <div class="pagination-container">
      <el-pagination
        :background="true"
        layout="prev, pager, next"
        v-model:current-page="state.postList.page"
        :total="state.postList.totalCount"
        :default-page-size="3"
        @current-change="(page: number) => getList(page)"
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
