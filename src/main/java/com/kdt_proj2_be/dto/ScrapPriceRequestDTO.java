package com.kdt_proj2_be.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kdt_proj2_be.domain.ScrapMetalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Getter @Setter
@NoArgsConstructor
public class ScrapPriceRequestDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate effectiveDate; // 날짜만 입력받고, 시간은 00:00:00으로 처리

    @JsonDeserialize(keyUsing = ScrapMetalTypeDeserializer.class) // ✅ ENUM 키 변환 추가
    private Map<ScrapMetalType, BigDecimal> prices; // 여러 고철 가격을 받기 위해 Map 사용

      // 수정 전
//    private ScrapMetalType scrapType; // ENUM 값으로 요청 받기
//    private BigDecimal price;
//    private LocalDateTime effectiveDate;
}