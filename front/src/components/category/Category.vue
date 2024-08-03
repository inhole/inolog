<script setup lang="ts">
import type Category from '@/entity/category/Category'
import { Delete } from '@element-plus/icons-vue'
import { useCategoryStore } from '@/stores/Category'

const props = defineProps<{
  category: Category
}>()

const store = useCategoryStore()
</script>

<template>
  <div class="common-layout">
    <el-container>
      <el-main>{{ props.category.name }}</el-main>

      <div class="delete">
        <el-button
          size="small"
          type="danger"
          :icon="Delete"
          @click="store.deleteCategory(category.id)"
          circle
        />
      </div>
    </el-container>
  </div>
</template>

<style scoped lang="scss"></style>
