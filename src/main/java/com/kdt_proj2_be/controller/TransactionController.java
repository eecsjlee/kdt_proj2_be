package com.kdt_proj2_be.controller;

import com.kdt_proj2_be.domain.ScrapMetalType;
import com.kdt_proj2_be.domain.Transaction;
import com.kdt_proj2_be.dto.TransactionDTO;
import com.kdt_proj2_be.dto.TransactionResponseDTO;
import com.kdt_proj2_be.service.ImageService;
import com.kdt_proj2_be.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final ImageService ImageService;

    // 입차시 등록
    @Operation(summary = "입차 등록", description = "입차 시 entryWeight와 scrapType을 포함하여 등록합니다.")
    @PostMapping
    public ResponseEntity<Transaction> registerTransaction(
            @RequestParam(name = "inImg1", required = false) MultipartFile inImg1file,
            @RequestParam("entryTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime entryTime,
            @RequestParam("entryWeight") BigDecimal entryWeight, // entryWeight 추가
            @RequestParam("scrapType") ScrapMetalType scrapType) throws IOException { // scrapType 추가

        String carNumber = null;
        MultipartFile inImg2file = null;
        MultipartFile inImg3file = null;

        // 클라이언트에서 받은 inImg1 파일이 있을 경우 Python 서버로 전송하고 응답받은 값을 사용
        if (inImg1file != null && !inImg1file.isEmpty()) {
            TransactionDTO pythonResponse = ImageService.sendImageToPythonServer(inImg1file);
            if (pythonResponse != null) {
                carNumber = pythonResponse.getCarNumber();
                inImg2file = pythonResponse.getInImg2();
                inImg3file = pythonResponse.getInImg3();
            }
            log.info("Python 서버 응답: carNumber={}, inImg2file={}, inImg3file={}", carNumber, inImg2file, inImg3file);
        }

        if (carNumber == null) {
            return ResponseEntity.badRequest().body(null);
        }

        // DTO 생성 및 입차 요청
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .carNumber(carNumber)
                .entryTime(entryTime)
                .entryWeight(entryWeight)
                .scrapType(scrapType) // scrapType 추가
                .inImg1(inImg1file)
                .inImg2(inImg2file)
                .inImg3(inImg3file)
                .build();

        Transaction newTransaction = transactionService.registerTransaction(transactionDTO);
        return ResponseEntity.ok(newTransaction);
    }

    @Operation(summary = "출차 처리", description = "가장 최근 거래 데이터에 업데이트합니다.")
    @PutMapping
    public ResponseEntity<Transaction> exitTransaction(
            @RequestParam(name = "outImg1", required = false) MultipartFile outImg1file,
            @RequestParam("exitTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime exitTime,
            @RequestParam("exitWeight") BigDecimal exitWeight) throws Exception {

        String carNumber = null;
        MultipartFile outImg2file = null;
        MultipartFile outImg3file = null;

        // Python 서버에서 차량 번호와 이미지 응답 받기
        if (outImg1file != null && !outImg1file.isEmpty()) {
            TransactionDTO pythonResponse = ImageService.sendImageToPythonServer(outImg1file);
            if (pythonResponse != null) {
                carNumber = pythonResponse.getCarNumber();
                outImg2file = pythonResponse.getInImg2();
                outImg3file = pythonResponse.getInImg3();
            }
            log.info("Python 서버 응답: carNumber={}, outImg2file={}, outImg3file={}", carNumber, outImg2file, outImg3file);
        }

        if (carNumber == null) {
            return ResponseEntity.badRequest().build(); // 차량 번호가 없으면 요청 실패
        }

        // DTO 생성 및 출차 요청
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .carNumber(carNumber)
                .exitTime(exitTime)
                .exitWeight(exitWeight)
                .outImg1(outImg1file)
                .outImg2(outImg2file)
                .outImg3(outImg3file)
                .build();

        Transaction updatedTransaction = transactionService.exitTransaction(transactionDTO);
        return ResponseEntity.ok(updatedTransaction);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }
}

