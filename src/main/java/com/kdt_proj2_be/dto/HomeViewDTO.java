package com.kdt_proj2_be.dto;

// HomeView 테이블 데이터를 담아 프론트에 전달함

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class HomeViewDTO {
    private String transactionStatus;
    private String carNumber;
    private String memberName;
    private String memberContact;
    private String scrapType;
    private BigDecimal totalWeight;
    private BigDecimal purchaseAmount;

//    @JsonSerialize(using = LocalDateTimeSerializer.class) // JSON 변환 시 포맷 적용
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime entryTime;

//    @JsonSerialize(using = LocalDateTimeSerializer.class) // JSON 변환 시 포맷 적용
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime exitTime;
}