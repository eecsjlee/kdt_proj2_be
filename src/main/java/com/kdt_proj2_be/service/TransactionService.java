package com.kdt_proj2_be.service;

import com.kdt_proj2_be.domain.MissingRecord;
import com.kdt_proj2_be.domain.ScrapPrice;
import com.kdt_proj2_be.domain.ScrapType;
import com.kdt_proj2_be.domain.Transaction;
import com.kdt_proj2_be.dto.EntryExitStatusDTO;
import com.kdt_proj2_be.dto.TransactionDTO;
import com.kdt_proj2_be.dto.TransactionResponseDTO;
import com.kdt_proj2_be.handler.MyWebSocketHandler;
import com.kdt_proj2_be.persistence.MissingRecordRepository;
import com.kdt_proj2_be.persistence.ScrapPriceRepository;
import com.kdt_proj2_be.persistence.ScrapTypeRepository;
import com.kdt_proj2_be.persistence.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j @Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ScrapPriceRepository scrapPriceRepository;
    private final ScrapTypeRepository scrapTypeRepository;
    private final MissingRecordRepository missingRecordRepository;
    private final MyWebSocketHandler webSocketHandler;
    private final ImageService imageService; // ImageService 주입

    public Transaction registerTransaction(TransactionDTO transactionDTO) throws IOException {

        // ScrapType 조회 (입력된 ENUM 값 기반)
        ScrapType scrapType = scrapTypeRepository.findByScrapType(transactionDTO.getScrapType())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 고철 종류입니다."));

        // Transaction 엔티티 생성
        Transaction transaction = Transaction.builder()
                .carNumber(transactionDTO.getCarNumber())
                .entryTime(transactionDTO.getEntryTime())
                .entryWeight(transactionDTO.getEntryWeight()) // entryWeight 추가
                .scrapType(scrapType) // scrapType 설정
                .updatedAt(LocalDateTime.now())
                .build();

        // 이미지 업로드
        String inImg1 = imageService.uploadImage(transactionDTO.getInImg1(), "inImg1");
        String inImg2 = imageService.uploadImage(transactionDTO.getInImg2(), "inImg2");
        String inImg3 = imageService.uploadImage(transactionDTO.getInImg3(), "inImg3");

        transaction.setInImg1(inImg1);
        transaction.setInImg2(inImg2);
        transaction.setInImg3(inImg3);

        return transactionRepository.save(transaction);
    }

    // 출차시 거래정보 등록
    public Transaction exitTransaction(TransactionDTO transactionDTO) throws Exception {
        String carNumber = transactionDTO.getCarNumber();
        BigDecimal exitWeight = transactionDTO.getExitWeight();
        LocalDateTime exitTime = transactionDTO.getExitTime();

        // 출차되지 않은 차량 조회 (entryTime이 있고 exitTime이 null)
        Optional<Transaction> transactionOpt = transactionRepository.findFirstByCarNumberAndExitTimeIsNullOrderByEntryTimeDesc(carNumber);

        // 입차 기록이 없는 경우 `MissingRecord`에 저장
        if (transactionOpt.isEmpty()) {
            log.warn("입차 기록이 없는 차량 발견: {}", carNumber);

            // 출차 이미지 업로드 (Base64 대신 URL 사용)
            String outImg1 = imageService.uploadImage(transactionDTO.getOutImg1(), "outImg1");
            String outImg2 = imageService.uploadImage(transactionDTO.getOutImg2(), "outImg2");
            String outImg3 = imageService.uploadImage(transactionDTO.getOutImg3(), "outImg3");

            // MissingRecord 테이블에 저장
            MissingRecord missingRecord = MissingRecord.builder()
                    .carNumber(carNumber)
                    .exitWeight(exitWeight)
                    .exitTime(exitTime)
                    .checkedAt(LocalDateTime.now())
                    .outImg1(outImg1)
                    .outImg2(outImg2)
                    .outImg3(outImg3)
                    .build();

            missingRecordRepository.save(missingRecord);

            throw new IllegalArgumentException("해당 차량의 최근 입차 기록이 없습니다. MissingRecord에 저장됨.");
        }

        // 입차 기록이 있는 경우 정상 출차 처리
        Transaction transaction = transactionOpt.get();

        // 출차 이미지 업로드
        String outImg1 = imageService.uploadImage(transactionDTO.getOutImg1(), "outImg1");
        String outImg2 = imageService.uploadImage(transactionDTO.getOutImg2(), "outImg2");
        String outImg3 = imageService.uploadImage(transactionDTO.getOutImg3(), "outImg3");

        // entryWeight와 exitWeight를 활용한 totalWeight 계산
        BigDecimal entryWeight = transaction.getEntryWeight();

        // `entryWeight` 또는 `exitWeight`가 `null`이면 예외 발생
        if (entryWeight == null || exitWeight == null) {
            throw new IllegalArgumentException("입차 중량(entryWeight) 또는 출차 중량(exitWeight)이 null입니다.");
        }

        // exitWeight가 entryWeight보다 크면 예외 발생 (정상적인 출차 데이터가 아님)
        if (exitWeight.compareTo(entryWeight) > 0) {
            throw new IllegalArgumentException("출차 중량이 입차 중량보다 클 수 없습니다.");
        }

        BigDecimal totalWeight = entryWeight.subtract(exitWeight); // 총 중량 계산

        // ScrapType을 이용하여 purchaseAmount 계산
        ScrapType scrapType = transaction.getScrapType();

        // 입차 시점(entryTime) 기준으로 해당 고철의 가격 조회
        ScrapPrice scrapPrice = scrapPriceRepository.findLatestPriceBeforeEntryTime(scrapType, transaction.getEntryTime())
                .orElseThrow(() -> new IllegalArgumentException("입차 시점의 해당 고철 가격 정보가 없습니다."));


        BigDecimal purchaseAmount = totalWeight.multiply(scrapPrice.getPrice()); // 구매 금액 계산

        // 출차 정보 업데이트
        transaction.setOutImg1(outImg1);
        transaction.setOutImg2(outImg2);
        transaction.setOutImg3(outImg3);
        transaction.setExitTime(transactionDTO.getExitTime());
        transaction.setExitWeight(exitWeight);
        transaction.setTotalWeight(totalWeight);
        transaction.setPurchaseAmount(purchaseAmount);
        transaction.setUpdatedAt(LocalDateTime.now());

        // 저장 후 반환
        return transactionRepository.save(transaction);
    }

    // 모든 거래 목록을 조회하는 서비스 메서드
    public List<TransactionResponseDTO> getAllTransactions() {

        // 데이터베이스
        List<Transaction> transactions = transactionRepository.findAll();

        // Entity → DTO 변환 후 반환
        return transactions.stream()
                .map(TransactionResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }


    public List<EntryExitStatusDTO> getEntryExitStatus() {
        return transactionRepository.findAllByOrderByUpdatedAtDesc()
                .stream()
                .map(EntryExitStatusDTO::fromEntity)
                .collect(Collectors.toList());
    }

}
