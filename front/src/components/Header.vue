<script setup lang="ts">
import { container } from 'tsyringe'
import UserRepository from '@/repository/UserRepository'
import { onBeforeMount, reactive } from 'vue'
import ProfileRepository from '@/repository/ProfileRepository'
import type UserProfile from '@/entity/user/UserProfile'

const USER_REPOSITORY = container.resolve(UserRepository)
const PROFILE_REPOSITORY = container.resolve(ProfileRepository)

const state = reactive<UserProfile | any>({
  profile: null
})

onBeforeMount(() => {
  USER_REPOSITORY.getProfile().then((profile) => {
    PROFILE_REPOSITORY.setProfile(profile)
    state.profile = profile
  })
})

function logout() {
  PROFILE_REPOSITORY.clear()
  location.href = '/api/logout'
}
</script>

<template>
  <div class="affix-container">
    <el-affix target=".affix-container" :offset="70" style="height: 100px">
      <div class="title">이노로그</div>
      <div class="menu">
        <router-link to="/">글 목록</router-link>
        <router-link v-if="state.profile !== null" to="/write">글 작성</router-link>
        <router-link v-if="state.profile == null" to="/login">로그인</router-link>
        <a href="#" v-else @click="logout()">({{ state.profile.name }}) 로그아웃</a>
      </div>
    </el-affix>
  </div>
</template>

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
