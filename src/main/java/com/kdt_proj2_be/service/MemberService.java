package com.kdt_proj2_be.service;

//회원 생성: 새 회원을 등록하는 로직.
//회원 조회: 특정 회원 정보를 조회하거나 전체 회원 목록을 반환하는 로직.
//회원 수정: 회원 정보를 수정하는 로직.
//회원 삭제: 회원을 삭제하는 로직.
//비즈니스 규칙 처리: 비즈니스 로직과 데이터 검증 처리.

import com.kdt_proj2_be.domain.Member;
import com.kdt_proj2_be.persistence.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원 가입
    public Member registerMember(Member member) {
        if (member.getBrn() == null || member.getBrn().isEmpty()) {
            throw new IllegalArgumentException("BRN must not be null or empty.");
        }

        member.setPassword(passwordEncoder.encode(member.getPassword()));
        return memberRepository.save(member); //회원 정보를 DB에 저장
    }

    // 회원 가입시 userId 중복 체크
    public boolean findMember(String userId) {
        Member findMember = memberRepository.findByUserId(userId).orElse(null);
        return findMember != null ? true : false;
    }

//    public boolean checkUsernameExists(String username) {
//        return memberRepository.existsByUsername(username); // 중복 여부 반환
//    }


    // 로그인 후 회원 정보 전달
    public ResponseEntity<Member> getUserData(Authentication authentication) {
        String userId = authentication.getName();
        Member member = memberRepository.findByUserId(userId).orElse(null);
        if (member != null) {
            return ResponseEntity.ok(member);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 회원 정보 수정
    public Member updateMember(Member member) {
        Member mem = memberRepository.findByUserId(member.getUserId()).orElseThrow(() -> new RuntimeException("Member not found"));
        if (member.getPassword() != null) {
            mem.setPassword(passwordEncoder.encode(member.getPassword()));
        }

        if (member.getName() != null) {
            mem.setName(member.getName());
        }

        return memberRepository.save(mem);
    }

    // 회원 비활성화 enabled를 false로 설정
    public void disableMember(String userId) {
        // userId를 기반으로 회원 정보 조회
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException(userId + "를 찾을 수 없습니다."));

        // 회원 비활성화 처리
        member.setEnabled(false);

        // 변경 사항 저장
        memberRepository.save(member);
    }

}
