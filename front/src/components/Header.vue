<template>
  <!--  <el-header class="header">-->
  <!--    <el-menu mode="horizontal" router>-->
  <!--      <el-menu-item index="/">Home</el-menu-item>-->
  <!--      <el-menu-item index="/login">로그인</el-menu-item>-->
  <!--      <el-menu-item index="/write">글 작성</el-menu-item>-->
  <!--    </el-menu>-->
  <!--  </el-header>-->

  <div class="affix-container">
    <el-affix target=".affix-container" :offset="70" style="height: 100px">
      <div class="title">이노로그</div>
      <div class="menu">
        <router-link :to="{ name: 'home' }">글 목록</router-link>
        <router-link :to="{ name: 'login' }">로그인</router-link>
        <router-link :to="{ name: 'write' }">글 작성</router-link>
      </div>
    </el-affix>
  </div>
</template>

<script setup lang="ts"></script>

<style scoped lang="scss">
.affix-container {
  text-align: center;
  //height: 200px;
  border-radius: 4px;
  //background: var(--el-color-primary-light-9);

  .title {
    margin-top: 1.5rem;
    margin-bottom: 0.4rem;
    font-size: 2rem;
  }

  .menu {
    a {
      color: inherit;
      text-decoration: none;
      margin: 1rem;
      font-size: 0.7rem;

      &:hover {
        text-decoration: underline;
      }
    }
  }
}
</style>
