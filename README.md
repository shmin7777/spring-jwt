# spring-jwt
JWT token을 사용해서 front-end단과 통신하는 example

### session방식 login
1. usernane, pw로 로그인이 성공한다면  
2. 서버쪽 session id 생성
3. 클라이언트 쿠키로 session ID를 저장
4. 요청할 때마다 쿠키값 sessionId를 항상 들고 서버쪽으로 요청하기 때문에
5. 서버는 세션ID가 유효한지 판단해서 유효하면 인증이 필요한 페이지로 접근하게 하면 됨.

### token 방식 login
1. usernane, pw로 로그인이 성공한다면  
2. JWT 토큰을 생성
3. 클라이언트 쪽으로 JWT 토큰을 응답
4. 요청할 때마다 JWT 토큰을 가지고 요청
5. 서버는 JWT토큰이 유효한지를 판단(필터를 만들어야함)


### session 방식의 문제점
1. 서버의 동접자 수가 많아진다면??
 * 서버를 늘린다. (로드 밸런싱한다)
 * 서버마다 session 공유가 안됨

2. 해결책으로는
* Sticky Session을 이용해 그 사용자가 갔던 서버로만 보냄
* 모든 서버에 세션이 생성 될 때마다 세션을 복제 
* DB를 세션 저장소로 사용한다. (DB에는 IO가 일어나기 때문에 속도가 엄청나게 저하된다. 세션은 메모리에 저장시키고 가져오기 때문에 훨씬 빠르다.) -> 메모리 서버를 이용한다! (주로 Redis를 이용한다.)
* JWT를 사용하면 이 문제들을 해결 할 수 있다.


# form login 방식이 사용안함
* formLogin().disable() 
* httpBasic().disable() // 기본 로그인 방식이 아닌 bearer라는 방식을 사용할 것이다.

#### bearer방식이란
![image](https://user-images.githubusercontent.com/67637716/231376700-d62d8d08-1413-41a6-bbf3-8158fc6feb5e.png)   


## login후 토큰 발급 과정
1. `UsernamePasswordAuthenticationFilter`를 구현한 `JwtAuthenticationFilter` 를 만든다.
2. securityConfig에서 `new JwtAuthenticationFilter(authenticationManager)` filter 등록
3. `UsernamePasswordAuthenticationFilter`의 `attemptAuthentication()`, `successfulAuthentication` override
4. attemptAuthentication - username, password 받아서 `authenticationManager.authenticate()`
5. 인증되면 session에 Authentication  저장 ( JWT를 쓰는데 SESSION쓰는 이유? : Security는 session에 Authentication이 있어야 권한 관리 해주기 때문 )
6. 인증이 완료되면 `successfulAuthentication()` 에서 JWT 만들어서 응답


## 토큰 발급
![image](https://user-images.githubusercontent.com/67637716/231396213-e466fe8a-2af2-4ec8-b6db-05be3cbc70c5.png)    

/login(POST)으로 요청을 보내면 응답 header의 Authorization에 jwt token이 들어가 있는걸 확인 할 수 있다.  
![image](https://user-images.githubusercontent.com/67637716/231396451-ec18b2e4-cf46-4532-8670-6eeb8faffc5e.png)  
