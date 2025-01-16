package com.kdt_proj2_be.service;

//회원 생성: 새 회원을 등록하는 로직.
//회원 조회: 특정 회원 정보를 조회하거나 전체 회원 목록을 반환하는 로직.
//회원 수정: 회원 정보를 수정하는 로직.
//회원 삭제: 회원을 삭제하는 로직.
//비즈니스 규칙 처리: 비즈니스 로직과 데이터 검증 처리.

import com.kdt_proj2_be.domain.Member;
import com.kdt_proj2_be.persistence.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 의존성 주입
//    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
//        this.memberRepository = memberRepository;
//        this.passwordEncoder = passwordEncoder;
//    }

    // 회원 가입
    public Member registerMember(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        return memberRepository.save(member);
    }

    // 회원 조회
    public Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found with ID: " + memberId));
    }

    // 회원 정보 수정
//    public Member updateMember(Member member) {
//        Member mem = memberRepository.findByUsername(member.getUsername());
//        if (member.getPassword() != null) {
//            mem.setPassword(passwordEncoder.encode(member.getPassword()));
//        }
//
//        if (member.getDisplayName() != null) {
//            mem.getDisplayName(member.getDisplayName());
//        }
//        return memberRepository.save(mem);
//    }

    // 회원 삭제
    public void deleteMember(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new RuntimeException("Member not found with ID: " + memberId);
        }
        memberRepository.deleteById(memberId);
    }

    // 중복 회원 검증
    private void validateDuplicateMember(Member member) {
        if (memberRepository.findByUsername(member.getUsername()).isPresent()) {
            throw new IllegalArgumentException(member.getUsername() + "는 이미 존재합니다.");
        }
    }
}
