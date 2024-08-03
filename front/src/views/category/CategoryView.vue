<script setup lang="ts">
import Category from '@/components/category/Category.vue'
import { useCategoryStore } from '@/stores/Category'
import { onMounted } from 'vue'

const store = useCategoryStore()

onMounted(() => {
  store.getList()
})
</script>

<template>
  <div class="content">
    <ul class="category">
      <li v-for="category in store.state.categoryList" :key="category.id">
        <Category :category="category" />
      </li>
    </ul>

    <el-form class="category-write" label-position="left">
      <el-form-item label="카테고리명">
        <el-input type="text" v-model="store.state.categoryWrite.name" style="width: 15%" />
        <el-button type="primary" class="button" @click="store.write()">추가</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<style scoped lang="scss">
.content {
  .category {
    list-style-type: none;
    padding-left: 0.5rem;
    li {
      //border: solid;
    }
  }

  .button {
    margin-left: 0.8rem;
  }
  .category-write {
    margin-top: 3rem;
  }
}
</style>
