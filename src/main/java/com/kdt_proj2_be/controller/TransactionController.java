package com.kdt_proj2_be.controller;

import com.kdt_proj2_be.domain.Transaction;
import com.kdt_proj2_be.dto.TransactionDTO;
import com.kdt_proj2_be.service.PythonImageService;
import com.kdt_proj2_be.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final PythonImageService pythonImageService;

    //차량등록
    @PostMapping
    public Transaction registerTransaction(
            @RequestParam(name = "inImg1", required = false) MultipartFile inImg1file,
            @RequestParam("entryTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime entryTime) throws IOException {

        String carNumber = null;
        MultipartFile inImg2file = null;
        MultipartFile inImg3file = null;

        // 클라이언트에서 받은 inImg1 파일이 있을 경우 Python 서버로 전송하고 응답받은 값을 사용
        if (inImg1file != null && !inImg1file.isEmpty()) {
            TransactionDTO pythonResponse = pythonImageService.sendImageToPythonServer(inImg1file);
            if (pythonResponse != null) {
                carNumber = pythonResponse.getCarNumber();
                inImg2file = pythonResponse.getInImg2();
                inImg3file = pythonResponse.getInImg3();
            }
            log.info("Python 서버 응답: carNumber={}, inImg2file={}, inImg3file={}", carNumber, inImg2file, inImg3file);
        }

        // DTO 생성 및 설정
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .entryTime(entryTime) // 입차 시간 추가
                .updatedAt(LocalDateTime.now()) // 업데이트 시간 추가
                .inImg1(inImg1file)
                .inImg2(inImg2file)
                .inImg3(inImg3file)
                .carNumber(carNumber)
                .build();

        // CarService 호출로 업데이트 처리
        return transactionService.registerTransaction(transactionDTO);
    }


    @PutMapping("/{transaction_id}/entry")
    public Transaction entryWeight(@RequestBody Transaction transaction) {
        return transactionService.entryWeight(transaction);
    }

    @PutMapping("/{transaction_id}/exit")
    public Transaction exitWeight(@RequestBody Transaction transaction) {
        return transactionService.exitWeight(transaction);
    }

//    @PutMapping("/{carNumber}/exit")
//    public Transaction putExitDate(
//            @PathVariable("carNumber") String carNumber,
//            @RequestParam("exitWeight") BigDecimal exitWeight) {
//        return transactionService.putExitDate(carNumber, exitWeight);
//    }



    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }
}

