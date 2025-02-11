package com.kdt_proj2_be.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kdt_proj2_be.domain.Role;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @ToString
@NoArgsConstructor // 기본 생성자를 자동으로 생성
public class MemberDTO {
    private String brn; // 사업자 번호
    private String userId;
    private String name; // 상호명
    private String emailAddress;
    private String postalCode;
    private String address;
    private String detailAddress;
    private String contact; // 연락처
    private Role role;
    private boolean enabled;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;


    MemberDTO(String a, String b) {
        this.userId = a;
        this.name = b;
    }
}
