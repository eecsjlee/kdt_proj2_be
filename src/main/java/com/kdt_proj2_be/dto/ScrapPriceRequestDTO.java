package com.kdt_proj2_be.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.kdt_proj2_be.domain.ScrapMetalType;
import com.kdt_proj2_be.util.EnumUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Getter @Setter
@NoArgsConstructor
public class ScrapPriceRequestDTO {

    private LocalDate effectiveDate; // 날짜만 입력받음 나중에 시간 00:00:00을 추가하여 LocalDateTime 형태로 변환

    @JsonDeserialize(keyUsing = EnumUtil.class) // ENUM 키 변환 추가
    private Map<ScrapMetalType, BigDecimal> prices; // 여러 고철 가격을 받기 위해 Map 사용
}