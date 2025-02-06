package com.kdt_proj2_be.controller;

import com.kdt_proj2_be.domain.Member;
import com.kdt_proj2_be.domain.Role;
import com.kdt_proj2_be.persistence.MemberRepository;
import com.kdt_proj2_be.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j; // 로깅 기능을 위한 Lombok 어노테이션 사용

@Slf4j // 로깅 기능 추가 (Lombok 사용)
@RestController
@RequiredArgsConstructor
@Tag(name = "member API", description = "멤버 정보를 담당하는 API")
@RequestMapping("/member")
public class MemberController {

    // 생성자 주입
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    // 유저가 입력한 아이디 비밀번호 보여지는 이름을 DB에 저장
    // DI SecurityConfig에 암호화시키는 함수 passwordEncoder.encode()
//    @PostMapping("/member")
//    String addMember(String username, String password, String displayname) {
//        Member member = new Member();
//        member.setUsername(username);
//        var hash = passwordEncoder.encode(password);
//        member.setPassword(hash);
//        member.setDisplayname(displayname);
//        member.setRole(Role.ROLE_MEMBER);
//
//        memberRepository.save(member);
//        return "redirect:/list"; //회원 등록 후 list로 페이지 리다이렉트함
//    }

    // 회원 가입 데이터를 받아서 MemberService로 보냄

//    @PostMapping
//    public ResponseEntity<Member> registerMember(@RequestBody Member member) {
//        return ResponseEntity.ok(memberService.registerMember(member));
//    }

    @PostMapping
    @Operation(
            summary = "회원 가입",
            description = "새 회원을 생성합니다."
    )
    public Member registerMember(@RequestBody Member member) {
        return memberService.registerMember(member);
    }

    @GetMapping("/exists")
    public boolean findMember(String userId) {
        System.out.println("MemberController findMember"); //확인용
        return memberService.findMember(userId);
    }

//    // 스프링부트3 교재 내용 p259
//    @GetMapping("/logout")
//    public String logout(HttpServletRequest request, HttpServletResponse response) {
//        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
//        return "redirect:/login";
//    }

    // 로그인시 회원 정보 열람
    @GetMapping
    public ResponseEntity<Member> getUserData(Authentication authentication) {
        return memberService.getUserData(authentication);
    }

    // 회원정보 수정
    @PutMapping
    public Member updateMember(@RequestBody Member member) {
        return memberService.updateMember(member);
    }

    // 회원 비활성화(탈퇴)
    @PatchMapping("/{userId}/disable")
    public ResponseEntity<String> disableMember(@PathVariable String userId) {
        memberService.disableMember(userId);
        return ResponseEntity.ok("Member has been disabled successfully.");
    }
}
