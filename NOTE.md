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