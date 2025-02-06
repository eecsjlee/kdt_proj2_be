package com.kdt_proj2_be.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdt_proj2_be.domain.Member;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;

@Slf4j // 로그 기록을 위한 Lombok 어노테이션
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;


    /**
     * 로그인 요청이 들어오면 사용자 인증을 시도하는 매서드
     * 사용자가 POST /login 요청시 실햄됨
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        log.info("JWTAuthenticationFilter attemptAuthentication 실행됨"); // 확인용

        // ObjectMapper를 사용하여 JSON 데이터를 Admin 객체로 변환
        ObjectMapper mapper = new ObjectMapper();
        try {

            // HTTP 요청에서 JSON 데이터를 읽어 Admin 객체로 변환
            Member member = mapper.readValue(request.getInputStream(), Member.class);

            // 사용자명과 비밀번호를 기반으로 인증 토큰 생성
            Authentication authToken = new UsernamePasswordAuthenticationToken(member.getUserId(), member.getPassword());

            // Spring Security의 AuthenticationManager를 통해 인증 수행
            return authenticationManager.authenticate(authToken);
        }
        catch(Exception e) {
            log.info(e.getMessage()); // 인증 실패 로그 출력
            response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 인증 실패시 401 응답 코드 반환
        }
        return null; // 인증 실패시 null 반환
    }

    /**
     * 로그인 성공시 실행되는 메서드
     * 사용자 정보를 기반으로 JWT를 생성하고 응답
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) {
        log.info("JWTAuthenticationFilter successfulAuthentication"); // 확인용
        User user = (User)authResult.getPrincipal();

        // JWT 토큰 생성
        String token = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000*60*100)) // 토큰의 유효기간 100분
                .withClaim("username",user.getUsername())
                .sign(Algorithm.HMAC256("com.pnu.jwt")); // HMAC256알고리즘을 사용해 토큰 서명 추가 "com.kdt_proj2_be.jwt"는 비밀키

        // 응답 헤더에 JWT 추가
        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token); // 클라이언트에게 토큰을 보냄 ( HTTP 응답 헤더에 JWT 토큰을 포함)
        // Authorization: Bearer <JWT 토큰> 이 형태(Bearer은 인증 방식을 뜻함)
        response.setStatus(HttpStatus.OK.value()); // 상태 코드 설정: 이 코드는 HTTP 응답 상태 코드를 200 OK로 설정
//        response.getWriter().write(user.getUsername()); // 응답 바디에 사용자명 추가
    }
}