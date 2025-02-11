package com.kdt_proj2_be.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kdt_proj2_be.domain.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDTO {

    private Long transactionId;
    private String carNumber;
    private String inImg1;
    private String inImg2;
    private String inImg3;
    private String outImg1;
    private String outImg2;
    private String outImg3;
    private String transactionStatus;
    private BigDecimal entryWeight;
    private BigDecimal exitWeight;
    private BigDecimal totalWeight;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime entryTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime exitTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    private BigDecimal purchaseAmount;
    private String scrapType; // ✅ scrapType을 문자열로 변환

    // ✅ Entity → DTO 변환 메서드 추가
    public static TransactionResponseDTO fromEntity(Transaction transaction) {
        return new TransactionResponseDTO(
                transaction.getTransaction_id(),
                transaction.getCarNumber(),
                transaction.getInImg1(),
                transaction.getInImg2(),
                transaction.getInImg3(),
                transaction.getOutImg1(),
                transaction.getOutImg2(),
                transaction.getOutImg3(),
                transaction.getTransactionStatus().name(), // ✅ Enum → String 변환
                transaction.getEntryWeight(),
                transaction.getExitWeight(),
                transaction.getTotalWeight(),
                transaction.getEntryTime(),
                transaction.getExitTime(),
                transaction.getUpdatedAt(),
                transaction.getPurchaseAmount(),
                transaction.getScrapType().getScrapType().name() // ✅ scrapType을 문자열로 변환
        );
    }
}
