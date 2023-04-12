# spring-jwt
JWT token을 사용해서 front-end단과 통신하는 example

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
