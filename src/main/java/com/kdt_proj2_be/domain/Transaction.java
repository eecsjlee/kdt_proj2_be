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

    private String carNumber;

    private String inImg1; // 이미지 파일
    private String inImg2; // 이미지 파일
    private String inImg3; // 이미지 파일

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status", nullable = true)
    private TransactionStatus transactionStatus; // 거래 상태 (enum 타입)

    private BigDecimal entryWeight; // 입차 중량

    private BigDecimal exitWeight; // 입차 중량

    private BigDecimal totalWeight;

    @Column(name = "entry_time", nullable = false)
    private LocalDateTime entryTime; // 입차 시간

    @Column(name = "exit_time")
    private LocalDateTime exitTime; // 출차 시간

    @UpdateTimestamp
    private LocalDateTime updatedAt;

//    @PrePersist
//    @PreUpdate
//    public void calculateWeightsAndAmount() {
//        // 출차 중량과 입차 중량이 유효한 경우 scrapWeight 계산
//        if (entryWeight != null && exitWeight != null) {
//            this.scrapWeight = entryWeight.subtract(exitWeight);
//        }
//
//        // scrapWeight와 scrapType이 유효한 경우 purchaseAmount 계산
//        if (scrapWeight != null && scrapType != null) {
//            BigDecimal price = getLatestPrice(scrapType);
//            if (price != null) {
//                this.purchaseAmount = price.multiply(scrapWeight);
//            }
//        }
//    }
//
//    private BigDecimal getLatestPrice(ScrapType scrapType) {
//        // 가장 최근 가격을 찾는 로직 (DB 조회 필요)
//        return scrapType.getLatestPrice(); // ScrapType 엔티티에서 가격을 가져오는 메서드 호출
//    }
}
