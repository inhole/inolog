import type UserProfile from '@/entity/user/UserProfile'
import { instanceToPlain } from 'class-transformer'
import { singleton } from 'tsyringe'

@singleton()
export default class ProfileRepository {
  // 서버에서 가져온 유저 정보 localStorage 에 담음
  public setProfile(profile: UserProfile) {
    const json = instanceToPlain(profile)
    localStorage.setItem('profile', JSON.stringify(json))
  }

  public clear() {
    localStorage.clear()
  }
}
