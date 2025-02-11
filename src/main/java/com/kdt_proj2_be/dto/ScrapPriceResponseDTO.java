package com.kdt_proj2_be.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;


/**
 * ScrapPriceResponseDTO는 고철 가격 정보를 클라이언트에게 전달하기 위한 데이터 전송 객체(DTO)입니다.
 * 이 객체는 스크랩 가격 적용 날짜와 각 스크랩 종류별 가격 정보를 포함합니다.
 */
@Getter @Setter
@AllArgsConstructor
public class ScrapPriceResponseDTO {

    private LocalDate effectiveDate;

    /**
     * 각 스크랩 종류별 가격 정보를 저장하는 Map
     *
     * Key: 고철의 종류를 나타내는 값 String
     * Value: 해당 고철의 가격을 나타내는 값 BigDecimal
     */
    private Map<String, BigDecimal> prices;
}
