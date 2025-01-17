package com.kdt_proj2_be.controller;

import com.kdt_proj2_be.domain.Member;
import com.kdt_proj2_be.domain.Role;
import com.kdt_proj2_be.persistence.MemberRepository;
import com.kdt_proj2_be.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
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
    @PostMapping
    public Member registerMember(@RequestBody Member member) {
//        Member member = new Member();
//        member.setUserId(userId);
//        var hash = passwordEncoder.encode(password);
//        member.setPassword(hash);
//        member.setName(name);
//        member.setRole(Role.ROLE_MEMBER);
        return memberService.registerMember(member);
    }

    @GetMapping("/checkDuple")
    public boolean findMember(String userId) {
        System.out.println("MemberController findMember"); //확인용
        return memberService.findMember(userId);
    }

    // 스프링부트3 교재 내용 p259
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }


//    @GetMapping("/mypage")
//    public String myPage(Authentication auth) {
//        CustomUser result = (CustomUser) auth.getPrincipal();
//        System.out.println(result.displayName);
//        return "mypage.html";
//    }

}
