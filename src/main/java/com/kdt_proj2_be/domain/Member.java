package com.kdt_proj2_be.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String displayname;
    private String password;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.ROLE_MEMBER;

}
