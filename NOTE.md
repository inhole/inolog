### Http Method 종류
 - GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD, TRACE, CONNECT

### 데이터를 검증하는 이유
 1. client 개발자가 깜빡할 수 있다. 실수로 값을 안보낼 수 있다.
 2. client bug로 값이 누락될 수 있다.
 3. 외부에 나쁜 사람이 값을 임의로 조작해서 보낼수 있다.
 4. DB에 값을 저장할 때 의도치 않은 오류가 발생할 수 있다.
 5. 서버 개발자의 편안함을 위해서

* DTO 에서 @NotBlank 검증 빈값 검증 굳이 검증 로직을 구현안해도 됨
* @ControllerAdvice, @ExceptionHandler 을 사용해 Exception 중앙 처리 클래스 생성
* 

### 빌더의 장점
- 가독성에 좋다 (값 생성에 대한 유연함)
- 필요한 값만 받을 수 있다. -> 오버로딩 가능한 조건 찾아보기
- 객체의 불변성

### TDD의 장점
- 초기에 문제 발견 가능
- 회귀 테스트 가능 (코드수정 / 리팩토링 시 기존 기능이 올바르게 작동하는지 확인 가능)
- 기능에 대한 불확실성 감소
- 시스템 문서 기능 역할을 수행할 수 있음

### API 문서 생성
- 클라이언트 입장 -> 어떤 API 있는지 모름

#### Spring RestDocs
- 운영코드에 영향이 가지 않고 테스트 코드로 작성 가능
- Test 케이스 실행 -> 문서 생성해준다
- 레퍼런스 참고 : https://docs.spring.io/spring-restdocs/docs/current/reference/htmlsingle/

#### 설정
1. build.gradle 설정
2. src/doc/asciidoc >> index.adoc 추가
3. bootJar 실행 하면 resources/static/docs >> index.html 생성
- 글 단건조회, 글 작성 함