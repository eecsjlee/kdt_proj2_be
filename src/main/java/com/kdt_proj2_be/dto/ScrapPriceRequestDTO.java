package com.kdt_proj2_be.dto;

import com.kdt_proj2_be.domain.ScrapMetalType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
public class ScrapPriceRequestDTO {
    private ScrapMetalType scrapType; // ENUM 값으로 요청 받기
    private BigDecimal price;
    private LocalDateTime effectiveDate;
}