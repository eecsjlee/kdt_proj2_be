package com.kdt_proj2_be.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Builder
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

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "scrap_type_id", nullable = false)
//    private ScrapType scrapType; // ScrapType 참조 (외래 키)

    @Column(name = "scrap_weight", nullable = false)
    private BigDecimal scrapWeight; // 계근

    @Column(name = "purchase_amount", nullable = false)
    private BigDecimal purchaseAmount; // 구매 금액

    @Column(name = "entry_date", nullable = false)
    private LocalDateTime entryDate; // 입차 시간

    @Column(name = "exit_date")
    private LocalDateTime exitDate; // 출차 시간
}
