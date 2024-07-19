import HttpRepository from '@/repository/HttpRepository'
import Login from '@/entity/user/Login'
import { inject, singleton } from 'tsyringe'
import UserProfile from '@/entity/user/UserProfile'

@singleton()
export default class UserRepository {
  constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {}

  // 로그인
  public login(request: Login) {
    return this.httpRepository.post({
      path: '/api/auth/login',
      body: request
    })
  }

  // 유저 정보 조회
  public getProfile() {
    return this.httpRepository.get<UserProfile>(
      {
        path: '/api/users/me'
      },
      UserProfile
    )
  }
}
