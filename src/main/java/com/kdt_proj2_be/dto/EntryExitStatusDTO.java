package com.kdt_proj2_be.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kdt_proj2_be.domain.Transaction;
import com.kdt_proj2_be.util.ImageUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class EntryExitStatusDTO {
    private String transactionStatus;
    private String carNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime entryTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime exitTime;

    private String inImg1;
    private String inImg2;
    private String inImg3;
    private String outImg1;
    private String outImg2;
    private String outImg3;

    private BigDecimal entryWeight;
    private BigDecimal exitWeight;
    private BigDecimal totalWeight;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;


    public static EntryExitStatusDTO fromEntity(Transaction transaction) {
        // base64 인코딩 없이 이미지 파일 이름만 전송하는 경우
//        return new EntryExitStatusDTO(
//                transaction.getTransactionStatus().name(),  // ENUM → String 변환
//                transaction.getCarNumber(),
//                transaction.getEntryTime(),
//                transaction.getExitTime(),
//                transaction.getInImg1(),
//                transaction.getInImg2(),
//                transaction.getInImg3(),
//                transaction.getOutImg1(),
//                transaction.getOutImg2(),
//                transaction.getOutImg3()
//        );

            // Base64 방식
//        return new EntryExitStatusDTO(
//                transaction.getTransactionStatus().name(),  // ENUM → String 변환
//                transaction.getCarNumber(),
//                transaction.getEntryTime(),
//                transaction.getExitTime(),
//                ImageUtil.encodeImageToBase64(transaction.getInImg1()), // Base64 변환
//                ImageUtil.encodeImageToBase64(transaction.getInImg2()),
//                ImageUtil.encodeImageToBase64(transaction.getInImg3()),
//                ImageUtil.encodeImageToBase64(transaction.getOutImg1()),
//                ImageUtil.encodeImageToBase64(transaction.getOutImg2()),
//                ImageUtil.encodeImageToBase64(transaction.getOutImg3())
//        );

        return new EntryExitStatusDTO(
                transaction.getTransactionStatus().name(),
                transaction.getCarNumber(),
                transaction.getEntryTime(),
                transaction.getExitTime(),
                "/images/" + transaction.getInImg1(),  // URL 경로로 변환
                "/images/" + transaction.getInImg2(),
                "/images/" + transaction.getInImg3(),
                "/images/" + transaction.getOutImg1(),
                "/images/" + transaction.getOutImg2(),
                "/images/" + transaction.getOutImg3(),
                transaction.getEntryWeight(),   // 추가된 속성
                transaction.getExitWeight(),    // 추가된 속성
                transaction.getTotalWeight(),   // 추가된 속성
                transaction.getUpdatedAt()      // 추가된 속성
        );

    }
}
