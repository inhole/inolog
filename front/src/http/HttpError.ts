import type { AxiosError } from 'axios'

/**
 * 공통 HttpError 처리 클래스
 * 백엔드에서 내려주는 값 맵핑
 */
export default class HttpError {
  private readonly code: string
  private readonly message: string

  constructor(e: AxiosError<any>) {
    this.code = e.response?.data.code ?? '500'
    this.message = e.response?.data.message ?? '네트워크 상태가 않좋아잉...'
  }

  public getMessage() {
    return this.message
  }
}
