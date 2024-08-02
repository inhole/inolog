<script setup lang="ts">
import { usePostStore } from '@/stores/Post'

const store = usePostStore()
</script>

<template>
  <el-form label-position="top">
    <el-form-item label="제목">
      <el-input
        v-model="store.state.postWrite.title"
        size="large"
        type="text"
        placeholder="제목을 입력해주세요"
      />
    </el-form-item>

    <el-form-item label="내용">
      <el-input v-model="store.state.postWrite.content" type="textarea" rows="15" alt="내용" />
    </el-form-item>

    <el-form-item>
      <el-button type="primary" style="width: 100%" @click="store.write()">등록완료</el-button>
    </el-form-item>
  </el-form>
</template>

<style></style>
