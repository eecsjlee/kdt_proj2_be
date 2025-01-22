package com.kdt_proj2_be.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(name = "user_id", unique = true, nullable = false)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(name = "email_address", nullable = true)
    private String emailAddress;

    @Column(name = "car_number", nullable = true)
    private String carNumber;

    //생년월일
    @Column(nullable = true)
    private LocalDate birth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Gender gender;

    @Column(name = "postal_code", nullable = true)
    private String postalCode;

    @Column(nullable = true)
    private String address;

    @Column(name = "detail_address", nullable = true)
    private String detailAddress;

    //연락처
    @Column(nullable = true)
    private String contact;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.ROLE_MEMBER;

    @Builder.Default
    private boolean enabled = true;

    //생성 일자
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    //수정 일자
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;
}
