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
//        validateDuplicateMember(member); // 중복 회원 검증
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        return memberRepository.save(member); //회원 정보를 DB에 저장
    }

    // 회원 가입시 userId 중복 체크
    public boolean findMember(String userId) {
        System.out.println("MemberService findMember"); // 확인용
        Member findMember = memberRepository.findByUserId(userId).orElse(null);
        return findMember != null ? true : false;
    }



    // 회원 조회
    public Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found with ID: " + memberId));
    }

    // 회원 정보 수정
    public Member updateMember(Member member) {
        Member mem = memberRepository.findByUserId(member.getUserId()).get();
        if (member.getPassword() != null) {
            mem.setPassword(passwordEncoder.encode(member.getPassword()));
        }

        if (member.getName() != null) {
            mem.setName(member.getName());
        }

        return memberRepository.save(mem);
    }

//    // 회원 삭제 실제론 enabled를 0으로 만듬
//    public void deleteMember(Long memberId) {
//        Member delmem = memberRepository.findByUserId(member.getUserId()).get();
//        delmem = false
//        memberRepository.save(mem);
//    }

    // 중복 회원 검증
    private void validateDuplicateMember(Member member) {
        if (memberRepository.findByUserId(member.getUserId()).isPresent()) {
            throw new IllegalArgumentException(member.getUserId() + "는 이미 존재합니다.");
        }
    }
}
