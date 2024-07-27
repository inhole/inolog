import { Transform } from 'class-transformer'
import { DateTimeFormatter, LocalDateTime } from '@js-joda/core'

export default class Comment {
  public id = 0
  public author = ''
  public password = ''
  public content = ''

  @Transform(({ value }) => LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME))
  public regDate = LocalDateTime.now()

  public getDisplayRegDate() {
    return this.regDate.format(DateTimeFormatter.ofPattern('yyyy년 MM월 dd일 HH시'))
  }
}
