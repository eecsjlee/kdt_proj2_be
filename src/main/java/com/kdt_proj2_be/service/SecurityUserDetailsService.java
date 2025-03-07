package com.kdt_proj2_be.service;

// 1) 클라이언트가 username과 password로 로그인 요청.
// 2) 스프링 시큐리티가 loadUserByUsername 메서드를 호출.
// 3) 이 메서드가 MemberRepository를 통해 사용자 정보를 조회.
// 4) 조회한 정보를 UserDetails 객체로 반환.
// 5) Spring Security는 반환된 객체를 사용해 비밀번호를 확인하고 권한을 검사

import com.kdt_proj2_be.domain.Member;
import com.kdt_proj2_be.persistence.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;


    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        // userId으로 Member를 찾고 없으면 예외를 던짐
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException(userId + "을 찾을 수 없습니다."));

        return new User(
                member.getUserId(), // 사용자명
                member.getPassword(), // 비밀번호
                Collections.singletonList(new SimpleGrantedAuthority(member.getRole().name())) // 권한 설정
        );
    }
}
