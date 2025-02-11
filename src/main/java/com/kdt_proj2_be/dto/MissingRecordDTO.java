package com.kdt_proj2_be.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MissingRecordDTO {
    private String carNumber;
    private LocalDateTime checkedAt;
    private LocalDateTime exitTime;
    private BigDecimal exitWeight;
}