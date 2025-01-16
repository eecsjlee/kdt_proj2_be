package com.kdt_proj2_be.controller;

import com.kdt_proj2_be.domain.Member;
import com.kdt_proj2_be.persistence.MemberRepository;
import com.kdt_proj2_be.service.MyUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
    @PostMapping("/member")
    String addMember(String username, String password, String displayName) {
        Member member = new Member();
        member.setUsername(username);
        var hash = passwordEncoder.encode(password);
        member.setPassword(hash);
        member.setDisplayName(displayName);

        memberRepository.save(member);
        return "redirect:/list"; //회원 등록 후 list로 페이지 리다이렉트함
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

//    @GetMapping("/mypage")
//    public String myPage(Authentication auth) {
//        CustomUser result = (CustomUser) auth.getPrincipal();
//        System.out.println(result.displayName);
//        return "mypage.html";
//    }

}
