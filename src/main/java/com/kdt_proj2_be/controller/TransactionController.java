package com.kdt_proj2_be.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.kdt_proj2_be.domain.Car;
import com.kdt_proj2_be.domain.RequestStatus;
import com.kdt_proj2_be.domain.Transaction;
import com.kdt_proj2_be.dto.TransactionDTO;
import com.kdt_proj2_be.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String pythonServerUrl = "http://10.125.121.214:5000/process_image";


    //차량등록
    @PostMapping
    public Transaction registerTransaction(
            @RequestParam(name = "inImg1", required = false) MultipartFile inImg1file,
            @RequestParam("entryTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime entryTime) throws IOException {

        // Python 서버에 이미지 전송
        JsonNode pythonResponse = null;
        if (inImg1file != null && !inImg1file.isEmpty()) {
            pythonResponse = sendImageToPythonServer(inImg1file);
            log.info("Python server response: {}", pythonResponse);
        }

        // DTO 생성 및 설정
        TransactionDTO transactionDTO = TransactionDTO.builder()
                .entryTime(entryTime) // 입차 시간 추가
                .updatedAt(LocalDateTime.now()) // 업데이트 시간 추가
                .inImg1(inImg1file)
                .build();

        // CarService 호출로 업데이트 처리
        return transactionService.registerTransaction(transactionDTO);
    }

    /**
     * Python 서버로 이미지 전송하는 메서드
     */
    private JsonNode sendImageToPythonServer(MultipartFile file) throws IOException {
        // HttpHeaders 객체를 생성하여 요청 헤더를 설정
        HttpHeaders headers = new HttpHeaders();

        // 파일을 보내기 위한 Content-Type을 multipart/form-data로 설정
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // MultipartFile을 전송할 본문을 생성하기 위한 MultiValueMap
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        // 파일을 ByteArrayResource로 감싸서 전송할 준비
        body.add("file", new org.springframework.core.io.ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });

        // HttpEntity 객체 생성: 헤더와 본문을 결합하여 요청 엔티티 구성
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // RestTemplate를 사용하여 Python 서버로 POST 요청을 전송
        // 요청은 'pythonServerUrl'에 POST 방식으로 전송되고, 응답은 JsonNode 형태로 반환받음
        ResponseEntity<JsonNode> response = restTemplate.exchange(
                pythonServerUrl, HttpMethod.POST, requestEntity, JsonNode.class);

        // 서버 응답의 본문을 JsonNode 형태로 반환
        return response.getBody();
    }

    @PatchMapping("/{transaction_id}/entry")
    public Transaction entryWeight(@RequestBody Transaction transaction) {
        return transactionService.entryWeight(transaction);
    }

    @PatchMapping("/{transaction_id}/exit")
    public Transaction exitWeight(@RequestBody Transaction transaction) {
        return transactionService.exitWeight(transaction);
    }

//    @PatchMapping("/{carNumber}/exit")
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
