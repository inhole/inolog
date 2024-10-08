import { DateTimeFormatter, LocalDateTime } from '@js-joda/core'
import { Transform } from 'class-transformer'

export default class Post {
  public id = 0
  public title = ''
  public content = ''
  public categoryName = ''
  public hits = 0

  @Transform(({ value }) => LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME))
  public regDate = LocalDateTime.now()

  public getDisplayRegDate() {
    return this.regDate.format(DateTimeFormatter.ofPattern('yyyy년 MM월 dd일 HH시'))
  }

  public getDisplaySimpleRegDate() {
    return this.regDate.format(DateTimeFormatter.ofPattern('yyyy-MM-dd'))
  }

  public getDisplayContent() {
    if (this.content.length > 200) {
      return this.content.substring(0, 200) + ' ...'
    }
    return this.content
  }
}
