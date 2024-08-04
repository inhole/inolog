<script setup lang="ts">
import { usePostStore } from '@/stores/Post'
import { useCategoryStore } from '@/stores/Category'
import { onMounted } from 'vue'

const postStore = usePostStore()
const categoryStore = useCategoryStore()

onMounted(() => {
  categoryStore.getList()
})
</script>

<template>
  <nav class="nav">
    <ul>
      <li>
        <el-link type="info" @click="postStore.getAside()">글 전체</el-link>
      </li>
      <li v-for="category in categoryStore.state.categoryList" :key="category.id">
        <el-link type="info" @click="postStore.getAside(category.id)">{{ category.name }}</el-link>
      </li>
    </ul>
  </nav>
</template>

<style scoped lang="scss">
.nav {
  margin-top: 1rem;
  ul {
    list-style-type: none;
    padding-left: 2rem;
    li {
      margin-bottom: 1rem;

      &:last-child {
        margin-bottom: 0;
      }
    }
  }
}
</style>
