package com.kdt_proj2_be.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.security.Timestamp;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_number", nullable = false)
    private Car car; // 차량(Car) 엔티티와 다대일 관계

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status", nullable = false)
    private TransactionStatus transactionStatus; // 거래 상태 (enum 타입)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scrap_type_id", nullable = false)
    private ScrapType scrapType; // ScrapType 참조 (외래 키)

    @Column(name = "scrap_weight", nullable = false)
    private BigDecimal scrapWeight; // 계근

    @Column(name = "purchase_amount", nullable = false)
    private BigDecimal purchaseAmount; // 구매 금액

    @Column(name = "entry_date", nullable = false)
    private LocalDateTime entryDate; // 입차 시간

    @Column(name = "exit_date")
    private LocalDateTime exitDate; // 출차 시간

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void calculatePurchaseAmount() {
        if (scrapWeight != null && scrapType != null) {
            BigDecimal price = getLatestPrice(scrapType);
            if (price != null) {
                this.purchaseAmount = price.multiply(scrapWeight);
            }
        }
    }

    private BigDecimal getLatestPrice(ScrapType scrapType) {
        // 가장 최근 가격을 찾는 로직 (DB 조회 필요)
        return scrapType.getLatestPrice(); // ScrapType 엔티티에서 가격을 가져오는 메서드 호출
    }

}
