package com.kdt_proj2_be.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Getter @Setter
@ToString @Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transaction_id; // 기본 키

//    @ManyToOne
//    @JoinColumn(name = "carNumber", nullable = false) //외래 키 매핑
//    private Car car;

    private String carNumber;

    private String inImg1; // 이미지 파일
    private String inImg2; // 이미지 파일
    private String inImg3; // 이미지 파일

    private String outImg1; // 이미지 파일
    private String outImg2; // 이미지 파일
    private String outImg3; // 이미지 파일

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "transaction_status", nullable = true)
    private TransactionStatus transactionStatus = TransactionStatus.PENDING; // 승인 상태 (enum 타입)

    private BigDecimal entryWeight; // 입차 중량

    private BigDecimal exitWeight; // 입차 중량

    private BigDecimal totalWeight;

    @Column(name = "entry_time", nullable = false)
    private LocalDateTime entryTime; // 입차 시간

    @Column(name = "exit_time")
    private LocalDateTime exitTime; // 출차 시간

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private BigDecimal purchaseAmount;

    // scrapTypeId를 외래키(FK)로 설정
    @ManyToOne
    @JoinColumn(name = "scrap_type_id", nullable = false) // FK 설정
    private ScrapType scrapType;

}
