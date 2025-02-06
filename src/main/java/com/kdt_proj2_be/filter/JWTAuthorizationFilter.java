package com.kdt_proj2_be.filter;

// 이 코드는 Spring Security에서 JWT 기반 인증 및 권한 부여(Authorization)를 처리하기 위한 필터 클래스입니다.
// JWTAuthorizationFilter는 요청이 들어올 때 사용자가 제공한 JWT를 확인하여 인증된 사용자인지 확인하고, 권한(Role)을 기반으로 인가(Authorization)를 수행합니다.


import com.kdt_proj2_be.domain.Member;
import com.kdt_proj2_be.persistence.MemberRepository;
import com.kdt_proj2_be.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.userdetails.User;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final MemberRepository memberRepository;

    // 순차적으로 요청을 처리할수 있는 각 필터
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("JWTAuthorizationFilter doFilterInternal");  // 버그 체크 용도
        String srcToken = request.getHeader("Authorization");
        if(srcToken == null || !srcToken.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = srcToken.replace("Bearer ",""); // 토큰에서 bearer제거후 문자열 저장

        if (JWTUtil.isExpired(jwtToken)) {
            System.out.println("JWT Token has expired"); // 응답 상태 코드 설정: 401 Unauthorized
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드
            return;
        }
        // build()검증객체 생성 verify 토큰 검증 username의 클레임값을 문자열로 반환
        String userId = JWT.require(Algorithm.HMAC256("com.pnu.jwt")).build().verify(jwtToken).getClaim("username").asString();

        Optional<Member> opt = memberRepository.findByUserId(userId);
        if(!opt.isPresent()) {
            filterChain.doFilter(request, response);
            return;
        }

        Member findmember = opt.get();
        // DB에서 읽은 사용자 정보를 이용해서 UserDetails 타입의 객체를 생성
        User user = new User(findmember.getUserId(),findmember.getPassword(),
                AuthorityUtils.createAuthorityList(findmember.getRole().toString()));
        // Authentication 객체를 생성 : 사용자명과 권한 관리를 위한 정보를 입력(암호는 필요 없음)
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        // 시큐리티 세션에 등록
        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);

    }
}
