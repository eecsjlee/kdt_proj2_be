package com.kdt_proj2_be.service;

//회원 생성: 새 회원을 등록하는 로직.
//회원 조회: 특정 회원 정보를 조회하거나 전체 회원 목록을 반환하는 로직.
//회원 수정: 회원 정보를 수정하는 로직.
//회원 삭제: 회원을 삭제하는 로직.
//비즈니스 규칙 처리: 비즈니스 로직과 데이터 검증 처리.

import com.kdt_proj2_be.domain.Member;
import com.kdt_proj2_be.persistence.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    // Constructor-based Dependency Injection
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 회원 등록
    public Member createMember(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        return memberRepository.save(member);
    }

    // 회원 조회
    public Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found with ID: " + memberId));
    }

    // 전체 회원 조회
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // 회원 수정 픽스미
    public Member updateMember(Long memberId, Member updatedMember) {
        Member existingMember = getMemberById(memberId);
        existingMember.setName(updatedMember.getName());
        existingMember.setEmail(updatedMember.getEmail());
        // 기타 필드 업데이트
        return memberRepository.save(existingMember);
    }

    // 회원 삭제
    public void deleteMember(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new RuntimeException("Member not found with ID: " + memberId);
        }
        memberRepository.deleteById(memberId);
    }

    // 중복 회원 검증
    private void validateDuplicateMember(Member member) {
        if (memberRepository.findByEmail(member.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Member with email " + member.getEmail() + " already exists.");
        }
    }
}
