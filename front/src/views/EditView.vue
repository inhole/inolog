<script setup lang="ts">
import { defineProps, reactive } from 'vue'
import { useRouter } from 'vue-router'
import Post from '@/entity/post/Post'
import { container } from 'tsyringe'
import PostRepository from '@/repository/PostRepository'
import { ElMessage } from 'element-plus'
import type HttpError from '@/http/HttpError'

const router = useRouter()

const data = reactive({
  post: new Post()
})

const props = defineProps<{
  postId: number
}>()

const POST_REPOSITORY = container.resolve(PostRepository)

POST_REPOSITORY.get(props.postId).then((response) => {
  data.post = response
})

const edit = () => {
  POST_REPOSITORY.edit(props.postId, data.post)
    .then(() => {
      ElMessage({ type: 'success', message: '글 수정이 완료되었습니다.' })
      router.replace({ name: 'home' })
    })
    .catch((e: HttpError) => {
      ElMessage({ type: 'error', message: e.getMessage() })
    })
}
</script>

<template>
  <el-form label-position="top">
    <el-form-item label="제목">
      <el-input
        v-model="data.post.title"
        size="large"
        type="text"
        placeholder="제목을 입력해주세요"
      />
    </el-form-item>

    <el-form-item label="내용">
      <el-input v-model="data.post.content" type="textarea" rows="15" alt="내용" />
    </el-form-item>

    <el-form-item>
      <el-button type="warning" style="width: 100%" @click="edit()">수정완료</el-button>
    </el-form-item>
  </el-form>
</template>

<style></style>
