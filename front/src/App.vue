<script setup lang="ts">
import Header from '@/components/Header.vue'
import Content from '@/components/Content.vue'
import Aside from '@/components/Aside.vue'
</script>

<template>
  <div class="common-layout">
    <el-container>
      <el-header height="100px">
        <Header />
      </el-header>
      <el-container>
        <el-aside width="15%"><Aside /></el-aside>
        <el-main>
          <Content />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style lang="scss">
body {
  font-family: -apple-system, BlinkMacSystemFont, 'Apple SD Gothic Neo', 'Pretendard Variable',
    Pretendard, Roboto, 'Noto Sans KR', 'Segoe UI', 'Malgun Gothic', 'Apple Color Emoji',
    'Segoe UI Emoji', 'Segoe UI Symbol', sans-serif;
}
</style>
