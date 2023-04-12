package com.example.jwt.filter;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyFilter3 implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("init3");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("my filter3");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // 토큰을 만들었다고 가정
        // 토큰 : 코스
        // 토큰을 만들어줘야함, id, pw정상적으로 들어와서 로그인이 완료되면 토큰을 만들어주고 그걸 응답을 해준다.
        // 요청할 때 마다 header에 Authorization에 value값으로 토큰을 가지고 옴
        // 그때 토큰이 넘어오면 이 토큰이 내가 만든 토큰이 맞는지만 검증만 하면 됨. (RSA, HS256)
        req.setCharacterEncoding("UTF-8");
        if (req.getMethod().equals("POST")) {
            System.out.println("POST 요청됨");
            String header = req.getHeader("Authorization");
            System.out.println(header);

            if (!header.equals("cos")) {
                PrintWriter writer = res.getWriter();
                writer.print("인증 안됨");
                return;
            }
        }


        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
        System.out.println("destroy3");
    }
}
