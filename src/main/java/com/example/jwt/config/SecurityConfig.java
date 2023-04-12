package com.example.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.web.filter.CorsFilter;
import com.example.jwt.filter.JwtAuthenticationFilter;
import com.example.jwt.filter.MyFilter3;
import com.example.jwt.filter.MyFilter4;
import lombok.RequiredArgsConstructor;

// .httpBasic()
// header에 authorization에 id, pw를 모든 요청에 넣는다.
// 암호화가 안되기때문에 위험

// jwt : header에 authorization에 token을 넣는다.
// id와 pw를 통해서 token을 달고 요청을 하는 방식이 Bearer라는 방식( not Basic )

// @CrossOrigin // 인증이 필요하지` 않는 요청만 허용함, 인증이 필요한 요청은 이렇게 하면 안됌
//@EnableWebSecurity(debug = true) // 어떤 필터를 사용하고 있는지 출력해줌
 @EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CorsFilter corsFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();

        // session을 사용하지 않겠다.
        return http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // .addFilter(corsFilter) // security filter에 등록해야함
                // .addFilter(jwtAuthenticationFilter) // AuthenticationManager를
                .formLogin().disable() // form tag만들어서 login을 안하겠다. /login 을 security가 가로채지 못함
                .httpBasic().disable() // 기본 로그인 방식이 아닌 bearer라는 방식을 사용할 것이다.
                .apply(new httpConfigurer())
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/user/**")
                .access("hasRole('ROLE_USER')  or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/manager/**")
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**")
                .access(" hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .build();
    }

    public class httpConfigurer extends AbstractHttpConfigurer<httpConfigurer, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            // attemptAuthentication()에서 AuthenticationManager가 loadUser 호출
            AuthenticationManager authenticationManager =
                    http.getSharedObject(AuthenticationManager.class);
            http
                    .addFilter(corsFilter) // security filter에 등록해야함
                    .addFilter(new JwtAuthenticationFilter(authenticationManager));

            // security filter chain이 FilterRegistrationBean으로 등록된 filter보다 항상 먼저 실행됨
            http.addFilterAfter(new MyFilter4(), SecurityContextHolderFilter.class);
            http.addFilterBefore(new MyFilter3(), SecurityContextHolderFilter.class);

        }
    }


}
