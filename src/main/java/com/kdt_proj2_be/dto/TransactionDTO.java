package com.kdt_proj2_be.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kdt_proj2_be.domain.ScrapMetalType;
import com.kdt_proj2_be.domain.TransactionStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @ToString @Builder
public class TransactionDTO {

    private MultipartFile inImg1; // Image file
    private MultipartFile inImg2; // Image file
    private MultipartFile inImg3; // Image file

    private MultipartFile outImg1; // Image file
    private MultipartFile outImg2; // Image file
    private MultipartFile outImg3; // Image file

    private Long transactionId;
    private String carNumber;
    private TransactionStatus transactionStatus;
    private ScrapMetalType scrapType;
    private BigDecimal entryWeight;
    private BigDecimal exitWeight;
    private BigDecimal totalWeight;
    private BigDecimal purchaseAmount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime entryTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime exitTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

}
