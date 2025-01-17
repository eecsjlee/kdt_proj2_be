package com.kdt_proj2_be.dto;

import lombok.*;

@Getter @Setter @ToString
@Builder
public class MemberDTO {
    public String nameId;
    public String name;

    MemberDTO(String a, String b) {
        this.nameId = a;
        this.name = b;
    }
}
