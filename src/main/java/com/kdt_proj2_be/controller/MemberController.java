package com.kdt_proj2_be.controller;

import com.kdt_proj2_be.domain.Member;
import com.kdt_proj2_be.persistence.MemberRepository;
import com.kdt_proj2_be.service.MemberService;
import com.kdt_proj2_be.util.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j // 로깅 기능 추가 (Lombok 사용)
@RestController
@RequiredArgsConstructor
@Tag(name = "member API", description = "멤버 정보를 담당하는 API")
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    @Operation(
            summary = "회원 가입",
            description = "새 회원을 생성합니다."
    )
    public Member registerMember(@RequestBody Member member) {
        return memberService.registerMember(member);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Member member) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(member.getUserId(), member.getPassword())
        );
        String token = JWTUtil.getJWT(authentication.getName());

        // JSON 형태로 응답
        Map<String, String> response = new HashMap<>();
        response.put("token", "Bearer " + token);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/exists")
    public boolean findMember(String userId) {
        System.out.println("MemberController findMember"); //확인용
        return memberService.findMember(userId);
    }

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
    @PutMapping("/{userId}/disable")
    public ResponseEntity<String> disableMember(@PathVariable String userId) {
        memberService.disableMember(userId);
        return ResponseEntity.ok("Member has been disabled successfully.");
    }
}
