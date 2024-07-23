<script setup lang="ts">
import { Delete } from '@element-plus/icons-vue'
</script>

<template>
  <div class="comment">
    <el-container>
      <el-aside width="200px">
        <div class="author">밥돌이</div>
        <div class="regDate">2023-05-01 23:59:59</div>
      </el-aside>
      <el-main>나 밥돌맨인데 개추했다.</el-main>

      <div class="delete">
        <el-button size="small" type="danger" :icon="Delete" circle />
      </div>
    </el-container>
  </div>
</template>

<style scoped lang="scss">
.comment {
  margin: 1rem;
  width: 100%;

  .header {
    display: flex;
    justify-content: space-between;
  }

  .section {
    display: flex;
    flex-direction: column;
  }

  .author {
    font-weight: 600;
    font-size: 1.2rem;
  }

  .regDate {
    margin-top: 5px;
    color: #797979;
    font-size: 0.88rem;
  }

  .content {
    margin-top: 0.8rem;
  }

  .delete {
  }
}
</style>
