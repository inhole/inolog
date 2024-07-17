<script setup lang="ts">
import { onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'
import Post from '@/components/Post.vue'
import { container } from 'tsyringe'
import PostRepository from '@/repository/PostRepository'

const POST_REPOSITORY = container.resolve(PostRepository)

const router = useRouter()

const state = reactive<any>({
  postList: []
})

function getList() {
  POST_REPOSITORY.getList().then((postList) => {
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
    <ul class="posts">
      <li v-for="post in state.postList" :key="post.id">
        <Post :post="post" />
      </li>
    </ul>
  </div>
</template>

<style scoped lang="scss">
.content {
  height: calc(100vh - 60px - 2rem - 20px - 1.5rem);
  padding: 0 1rem 0 1rem;
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
</style>
