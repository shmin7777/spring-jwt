package com.example.jwt.constant;

public class JwtProperties {
    public static final String SECRET = "hong"; // 우리 서버만 알고 있는 비밀값
    public static final int EXPIRATION_TIME = 86400000; // 1일 (1/1000초)
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
