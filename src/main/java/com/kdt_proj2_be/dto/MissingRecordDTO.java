package com.kdt_proj2_be.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kdt_proj2_be.domain.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MissingRecordDTO {
    private String carNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime exitTime;

    private BigDecimal exitWeight;

    private String outImg1;
    private String outImg2;
    private String outImg3;

    public MissingRecordDTO(String carNumber, LocalDateTime checkedAt, LocalDateTime exitTime,
                            BigDecimal exitWeight, String outImg1, String outImg2, String outImg3) {
        this.carNumber = carNumber;
        this.checkedAt = checkedAt;
        this.exitTime = exitTime;
        this.exitWeight = exitWeight;

        // 파일 경로를 URL 형식으로 변환
        this.outImg1 = "/images/" + outImg1;  // 이미지 파일 이름을 URL 경로로 변환
        this.outImg2 = "/images/" + outImg2;
        this.outImg3 = "/images/" + outImg3;
    }
}