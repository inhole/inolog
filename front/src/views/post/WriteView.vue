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
  <el-form label-position="top">
    <el-form-item label="제목">
      <el-input
        v-model="postStore.state.postWrite.title"
        size="large"
        type="text"
        placeholder="제목을 입력해주세요"
      />
    </el-form-item>

    <el-form-item label="카테고리">
      <el-select v-model="postStore.state.postWrite.category.id" placeholder="선택">
        <el-option
          v-for="category in categoryStore.state.categoryList"
          :key="category.id"
          :label="category.name"
          :value="category.id"
        />
      </el-select>
    </el-form-item>

    <el-form-item label="내용">
      <el-input v-model="postStore.state.postWrite.content" type="textarea" rows="15" alt="내용" />
    </el-form-item>

    <el-form-item>
      <el-button type="primary" style="width: 100%" @click="postStore.write()">등록완료</el-button>
    </el-form-item>
  </el-form>
</template>

<style></style>
