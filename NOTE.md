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
4. test/resources 에 보면 커스텀 템플릿 생성 가능
- 글 단건조회, 글 작성 함

### ssh-keygen 설정
- github 같은 경우 SSH and GPG keys 에 들어간 후
- local 깃 bash 등 으로 ssh-keygen 실행
- 생성 된 id_rsa.pub 공개키 >> cat id_rsa.pub 붙여넣기 
- ssh키로 clone

### vue 설치
```sh
$ npm init vue@latest
Need to install the following packages:
create-vue@3.10.4
Ok to proceed? (y) y
Vue.js - The Progressive JavaScript Framework
√ Project name: ... front
√ Add TypeScript? ... Yes
√ Add JSX Support? ... Yes
√ Add Vue Router for Single Page Application development? ... Yes
√ Add Pinia for state management? ... Yes
√ Add Vitest for Unit Testing? ... No
√ Add an End-to-End Testing Solution? » No
√ Add ESLint for code quality? ... Yes
√ Add Prettier for code formatting? ... Yes
√ Add Vue DevTools 7 extension for debugging? (experimental) ... Yes
```
### 서버
1. 네트워크를 통해 정보나 서비스를 제공하는 장치 or 프로그램
2. 개인 PC
   - 접근성 좋음
   - 서버 구입 비용 없음
   - 서버 설정 직접 해야 함
   - 전기세 많이 나옴
   - 네트워크 속도
   - IP 주소 
3. 웹호스팅
   - 하나의 서버(OS)에 여러 사용자 입주
   - 가격이 저렴함 (월 500원~)
   - 환경 제한 (주로 PHP에서 많이 이용함)
   - 트래픽 제한
   - 속도 느림
   - 도메인 등록 제한
4. 서버 호스팅, 코로케이션
   - 호스팅 업체를 통해 서버를 대여 (구입도 가능)
   - 알고 보면 가성비
   - 서버 설정 직접 해야 함
   - 서버 월 8만원 vs 200만원 서버 월 30만원 1년 약정 인수
   - 인수 후에는 코로케이션 형태로 전환 (월 10만원)
   - 확장성 부족
5. 클라우드 (AWS, Azure)
   - 이용한 만큼 지불
   - 설정, 관리가 상대적으로 쉬움
   - 확장성
   - 비쌈
   - 비즈니스에 집중 가능 (어느 정도 까지는)
   - 알고 보면 저렴 (탄력적 운영 시간, 인력 등)

### AWS EC2 생성하기
- 새 인스턴스 생성
- OS: Amazon Linux 선택
- 인스턴스 유형 (프리 티어 사용 가능) / 월 15,000원 정도
- 키 페어(로그인: 비밀 키 파일) = RSA / .pem (.ssh 폴더에 저장 하는게 좋을 듯, chmod 600 권한 설정)
- 스토리지: 16GB
- 접속: ssh -i ~/.ssh/{키 페어 파일}.pem ec2-user@{ip}

### EC2 파일 전송
- scp file 이용 (로컬과 원격에 연결) or rsync 이용
- 명령어: scp -i ~/.ssh/{키 페어 파일}.pem ./{빌드된 파일}.jar ec2-user@{ip}:/home/ec2-user

### java 설치
- java 설치 (명령어: sudo amazon-linux-extras install {java}) / or yum, apt

### 인바운드 규칙
- 사용자 지정 TCP / AnyWhere-IPv4 / 8080

### curl 명령어로 간단한 api test
- nohup 명령어로 실행 하면 백그라운드에서 실행 가능
- nohup java -jar inolog.jar &
- 확인: netstat -lntp
- 
### 고정 IP 설정
- 탄력적 IP 주소 할당
- 탄력적 IP 주소 연결

---
### 요청 확인
- get param 으로 인증
- header 으로 인증
- post body 로 인증
- Interceptor 활용, HandlerMethodArgumentResolver 활용

### ModHeader
- 크롬 확장프로그램
- Http Header 값 추가

### 암호화 (Scrypt, Bcrypt)


### csrf (크로스 사이트 요청 위조)
- 인증된 사용자가 웹 애플리케이션에 특정 요청을 보내도록 유도하는 공격 행위
- 생성된 요청이 사용자의 동의를 받았는지 확인할 수 없는 웹 애플리케이션의 CSRF 취약점을 이용

### security
- role: 역할 -> 관리자, 사용자, 매니저
- authority: 권한 -> 글쓰기, 글 읽기, 사용자정지시키기