package com.kdt_proj2_be.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "missing_records")
public class MissingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordId;

    @Column(name = "car_number", nullable = true)
    private String carNumber; // 차량 번호

    @Column(name = "exit_weight", nullable = true)
    private BigDecimal exitWeight;

    @Column(name="exit_time", nullable = true)
    private LocalDateTime exitTime;

    @Column(name = "checked_at", nullable = false)
    private LocalDateTime checkedAt; // 언제 확인되었는지 기록
}