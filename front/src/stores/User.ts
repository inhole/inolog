import { reactive } from 'vue'
import { defineStore } from 'pinia'
import UserProfile from '@/entity/user/UserProfile'
import { container } from 'tsyringe'
import UserRepository from '@/repository/UserRepository'
import ProfileRepository from '@/repository/ProfileRepository'
import { instanceToPlain, plainToInstance } from 'class-transformer'

export const useUserStore = defineStore('user', () => {
  // 유저 정보 프로필
  const state = reactive<UserProfile | any>({
    profile: null
  })

  const USER_REPOSITORY = container.resolve(UserRepository)
  const PROFILE_REPOSITORY = container.resolve(ProfileRepository)

  function getProfile() {
    USER_REPOSITORY.getProfile().then((profile) => {
      const json = JSON.stringify(instanceToPlain(profile))
      if (json === null || json === '""') {
        return
      }
      PROFILE_REPOSITORY.setProfile(profile)
      state.profile = profile
    })
  }

  function logout() {
    PROFILE_REPOSITORY.clear()
    location.href = '/api/logout'
  }

  return { state, getProfile, logout }
})
