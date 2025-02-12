package com.kdt_proj2_be.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kdt_proj2_be.domain.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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


    public static EntryExitStatusDTO fromEntity(Transaction transaction) {
        return new EntryExitStatusDTO(
                transaction.getTransactionStatus().name(),  // ENUM → String 변환
                transaction.getCarNumber(),
                transaction.getEntryTime(),
                transaction.getExitTime(),
                transaction.getInImg1(),
                transaction.getInImg2(),
                transaction.getInImg3(),
                transaction.getOutImg1(),
                transaction.getOutImg2(),
                transaction.getOutImg3()
        );
    }
}
