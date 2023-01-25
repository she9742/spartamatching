### 프로젝트 소개

- 고객-판매자 매칭 서비스


### 우리 약속
- 12시 중간 회의 / 저녁 8시 마무리 회의 진행
- 커밋 메시지 ‘[작업명] 작업한내용 간략히’
- commit/push 전에 말하기
- 막히는 부분(최대 1시간 고민)있으면 팀원에게 바로 물어보기


### 개발 환경
    - Project : Gradle
    - Java 11
    - Spring Boot 2.7.7

<img src="">

### ERD 데이터 베이스 설계
<img src="https://s3.us-west-2.amazonaws.com/secure.notion-static.com/9a14e873-fe07-4cf1-9455-4f4689690d09/Untitled_%282%29.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20230125%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20230125T005556Z&X-Amz-Expires=86400&X-Amz-Signature=55270bbfa8267ace844d1277425eacb2e470405cec43bb66716fec87c3687805&X-Amz-SignedHeaders=host&response-content-disposition=filename%3D%22Untitled%2520%282%29.png%22&x-id=GetObject">

### UML

<img src="https://s3.us-west-2.amazonaws.com/secure.notion-static.com/a6271166-4ef0-4006-953d-1667535f57e0/%ED%99%94%EB%A9%B4_%EC%BA%A1%EC%B2%98_2023-01-23_224706.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20230125%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20230125T005732Z&X-Amz-Expires=86400&X-Amz-Signature=6b90e8402eac4580a3fc949542fe3c8b41b46458425ef3f3c7d504f68135d7fd&X-Amz-SignedHeaders=host&response-content-disposition=filename%3D%22%25ED%2599%2594%25EB%25A9%25B4%2520%25EC%25BA%25A1%25EC%25B2%2598%25202023-01-23%2520224706.png%22&x-id=GetObject">


## API
| API 	| Method 	| URL 	| Request 	| Response 	|
|:---:	|---	|---	|---	|---	|
| 고객 로그아웃 	| POST 	| /users/signout 	| Header Authorization :<br>Bearer <JWT> 	| “로그아웃 되었습니다” 	|
| 회원가입 	| POST 	| /Client/signup 	| {“username” : “string”, “nickname” : “string”, “password” : “string”, “image”: “string”,} | “회원 가입이 완료되었습니다.” 	|
| 로그인 	| POST 	| /Client/signin 	| {“username” : “string”,“password” : “string” ,} | “로그인 되었습니다.” 	|
| 판매자 권한요청 	| POST 	| /Client/sellers 	| {Header Authorization : Bearer <JWT>{“about” : “string”,“category” : “string”} |“판매자권한 요청을 관리자에게 보냈습니다.”	|
| 프로필 설정 | Patch 	| /Client/profiles 	| Header Authorization : Bearer <JWT>{“about” : “string”,“image” : “string”}} |“프로필 설정 완료” 	|
| 프로필 조회 | Get 	| /Client/profiles 	| Header Authorization : Bearer |{“nickname” :  “string”“image” : “string”}	|
| 전체 상품 조회 	| Get 	| /Client/products	| Header Authorization :Bearer <JWT>{”page” : int,”size” : int,”sortBy” : String,”orderBy”: boolean} | {“productname” : “string”“category” : “string”“price” : “int”“sellername” : “string”}|
| 전체 판매자 조회 	| Get 	| /Client/sellers	| Header Authorization :Bearer <JWT>{”page” : int,”size” : int,”sortBy” : String,”orderBy”: boolean} | {“nickname” : “string”“image” : “string”“category” : “string”“about” : “string”}|
| 판매자 선택 조회 	| Get 	| /Client/sellers/{sellerId}	| Header Authorization :Bearer <JWT> | {“nickname” : “string”“image” : “string”“category”:“string”“about” : “string”}|
| 전체 메세지 조회 	| Get 	| /client/talks/{talkId}	| Header Authorization :Bearer <JWT> |{”writer” : ”String”,”message” : “String”}|
| 메시지 전송 	| Post 	| /client/talks/{talkId}	| Header Authorization :Bearer <JWT>{”content” : “String”} |{”writer” : ”String”,”message” : “String”}|
| 판매자에게 매칭 요청 	| Post 	| /client/matching/{productId}	| Header Authorization :Bearer <JWT>,sellerId |“판매자에게 매칭요청을보냈습니다.”|
| 물건 구매 요청 	| Post 	| /client/buy/{productId}	| Header Authorization :Bearer <JWT> |“물건을 구매하였습니다.”|
| 토큰 재발급 	| Post 	| /client/refresh	| Header Authorization :Bearer <JWT> |{”accessToken” : “String”,”refreshToken” : “String” }|






