package com.kdt_proj2_be.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
public class Member {

    @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "brn", nullable = false)
    private String brn; // 사업자 번호

    @Column(name = "user_id", unique = true, nullable = false)
    private String userId;

    @Column(nullable = false)
    private String password;

    // 상호명
    @Column(nullable = false)
    private String name;

    @Column(name = "email_address", nullable = true)
    private String emailAddress;

    @Column(name = "postal_code", nullable = true)
    private String postalCode;

    @Column(nullable = true)
    private String address;

    @Column(name = "detail_address", nullable = true)
    private String detailAddress;

    //연락처
    @Column(nullable = false)
    private String contact;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.ROLE_MEMBER;

    @Builder.Default
    private boolean enabled = true;

    //생성 일자
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    //수정 일자
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
