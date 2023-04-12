package com.example.jwt.filter;

import java.io.IOException;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.jwt.auth.PrincipalDetails;
import com.example.jwt.constant.JwtProperties;
import com.example.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

// 스프링 시큐리티에서 usernamePasswordAuthenticationFilter가 있음.
// /login 요청해서 username, password를 POST로 전송하면
// 이 필터가 동작함
// formLogin.disable() 해버렸기 때문에 작동을 하지 않음
// 이 필터를 다시 security config에 등록을 해줘야 함
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;


    /**
     * /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {
        System.out.println("로그인 시도");

        // 1. username, password 받아서
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            User user = objectMapper.readValue(request.getInputStream(), User.class);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            // 2. 정상인지 로그인 시도, authenticationManager로 로그인 시도를 하면
            // PrincipalDetailsService가 호출 loadUserByUsername() 함수 실행 됨
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);

            PrincipalDetails principalDetails = (PrincipalDetails) authenticate.getPrincipal();
            System.out.println(principalDetails);
            System.out.println("========================");

            // 3. PrincipalDetails를 seesion에 담는다.
            // session에 담는 이유? security session에 PrincipalDetails가 있어야 security가 권한 관리를 해줌
            // 굳이 JWT 토큰을 사용하면서 세션을 만들 이유가 없다. 단지 권한 처리 때문에 SESSION 넣어줌
            return authenticate; // authenticate는 session영역에 저장됨, 그 방법은 return 해주면 됨.

            // 4. JWT 토큰을 만들어서 응답(successfulAuthentication)
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



        return null;
    }


    /**
     * attemptAuthentication 실행 후 인증이 정상적으로 되었다면 successfulAuthentication 함수가 실행 JWT 토큰을 만들어서
     * request 요청한 사용자에게 JWT토큰을 RESPONSE 해주면 됨 4. JWT 토큰을 만들어서 응답
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        System.out.println("successfulAuthentication 실행됨, 인증이 완료됨");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // RSA(public, private key)방식은 아니고, HMAC256방식(Secret을 추가하여 hashing)
        String jwtToken = JWT.create()
                .withSubject("cos 토큰") // 토큰 이름
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME)) // 만료
                                                                                                     // 시간
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUsername())
                .sign(Algorithm.HMAC256(JwtProperties.SECRET)); // 전자 서명


        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);
    }

}


