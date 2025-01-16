package com.kdt_proj2_be.controller;

// 가입 페이지로 이동 register
// 로그인 페이지로 이동 login
// 로그아웃 페이지로 이동 logout

import com.kdt_proj2_be.domain.Member;
import com.kdt_proj2_be.domain.Role;
import com.kdt_proj2_be.persistence.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Controller
@RequiredArgsConstructor
public class MemberController {

    // 생성자 주입
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입 페이지 출력 요청
    @GetMapping("/register")
    String register() {
        return "register.html";
    }

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

    @PostMapping("/member")
    public String registerMember(String username, String password, String displayname) {
        Member member = new Member();
        member.setUsername(username);
        var hash = passwordEncoder.encode(password);
        member.setPassword(hash);
        member.setDisplayname(displayname);
        member.setRole(Role.ROLE_MEMBER);

        memberRepository.save(member);
        return "redirect:/list"; //회원 등록 후 list로 페이지 리다이렉트함
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
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
