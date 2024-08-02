<script setup lang="ts">
import { defineProps, onMounted } from 'vue'
import { usePostStore } from '@/stores/Post'

const props = defineProps<{
  postId: number
}>()

const store = usePostStore()

onMounted(() => {
  store.getPost(props.postId)
})
</script>

<template>
  <el-form label-position="top">
    <el-form-item label="제목">
      <el-input
        v-model="store.state.post.title"
        size="large"
        type="text"
        placeholder="제목을 입력해주세요"
      />
    </el-form-item>

    <el-form-item label="내용">
      <el-input v-model="store.state.post.content" type="textarea" rows="15" alt="내용" />
    </el-form-item>

    <el-form-item>
      <el-button type="warning" style="width: 100%" @click="store.editPost(postId)"
        >수정완료</el-button
      >
    </el-form-item>
  </el-form>
</template>

<style></style>
