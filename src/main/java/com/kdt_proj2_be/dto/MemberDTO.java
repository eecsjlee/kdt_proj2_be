package com.kdt_proj2_be.dto;

import lombok.*;

@Getter @Setter @ToString
@Builder
public class MemberDTO {
    public String username;
    public String displayName;

    MemberDTO(String a, String b) {
        this.username = a;
        this.displayName = b;
    }
}
