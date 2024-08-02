<script setup lang="ts">
import Category from '@/components/category/Category.vue'
import { useCategoryStore } from '@/stores/Category'

const store = useCategoryStore()
</script>

<template>
  <div class="content">
    <div class="category">
      <Category />
    </div>

    <el-form label-position="left">
      <el-form-item label="카테고리명">
        <el-input type="text" v-model="store.state.category.name" id="author"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" class="button" @click="store.write()">등록하기</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<style scoped lang="scss"></style>
