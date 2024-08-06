<script setup lang="ts">
import { onBeforeMount } from 'vue'
import { useUserStore } from '@/stores/User'

const store = useUserStore()

onBeforeMount(() => {
  store.getProfile()
})
</script>

<template>
  <div class="header-container">
    <div class="title">🎈이노로그</div>
    <div class="menu">
      <router-link to="/">글 목록</router-link>
      <router-link v-if="store.state.profile !== null" to="/write">글 작성</router-link>
      <router-link v-if="store.state.profile !== null" to="/category">카테고리</router-link>
      <router-link v-if="store.state.profile == null" to="/login">로그인</router-link>
      <a href="#" v-else @click="store.logout()">({{ store.state.profile.name }}) 로그아웃</a>
    </div>
  </div>
</template>

<style scoped lang="scss">
.header-container {
  text-align: center;

  .title {
    margin-top: 1.3rem;
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
