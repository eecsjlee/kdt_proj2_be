package com.kdt_proj2_be.service;

import com.kdt_proj2_be.domain.ScrapPrice;
import com.kdt_proj2_be.domain.ScrapType;
import com.kdt_proj2_be.domain.Transaction;
import com.kdt_proj2_be.dto.TransactionDTO;
import com.kdt_proj2_be.persistence.ScrapPriceRepository;
import com.kdt_proj2_be.persistence.ScrapTypeRepository;
import com.kdt_proj2_be.persistence.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ScrapPriceRepository scrapPriceRepository;
    private final ScrapTypeRepository scrapTypeRepository;

    // Image upload method
    private String uploadImage(MultipartFile file, String prefix) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String absolutePath = new File("").getAbsolutePath() + File.separator;
        String path = "src/main/resources/static/images";
        File imgDir = new File(path);
        if (!imgDir.exists()) {
            imgDir.mkdirs();
        }

        String contentType = file.getContentType();
        String originalFileName = file.getOriginalFilename();
        int lastIndex = originalFileName.lastIndexOf('.') + 1;
        String fileName = originalFileName.substring(0, lastIndex - 1);
        String ext = originalFileName.substring(lastIndex);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String newFileName = prefix + "_" + sdf.format(new Date()) + "." + ext;

        // Save the file
        file.transferTo(new File(absolutePath + path + File.separator + newFileName));
        return newFileName;
    }


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
        String inImg1 = uploadImage(transactionDTO.getInImg1(), "inImg1");
        String inImg2 = uploadImage(transactionDTO.getInImg2(), "inImg2");
        String inImg3 = uploadImage(transactionDTO.getInImg3(), "inImg3");

        transaction.setInImg1(inImg1);
        transaction.setInImg2(inImg2);
        transaction.setInImg3(inImg3);

        return transactionRepository.save(transaction);
    }

    // 출차시 거래정보 등록
    public Transaction exitTransaction(TransactionDTO transactionDTO) throws IOException {
        String carNumber = transactionDTO.getCarNumber();

        // 🚗 출차되지 않은 최신 거래 조회 (최근 entryTime 기준)
        Transaction transaction = transactionRepository.findLatestTransactionByCarNumber(carNumber)
                .orElseThrow(() -> new IllegalArgumentException("해당 차량의 최근 입차 기록이 없습니다."));

        // 📸 출차 이미지 업로드
        String outImg1 = uploadImage(transactionDTO.getOutImg1(), "outImg1");
        String outImg2 = uploadImage(transactionDTO.getOutImg2(), "outImg2");
        String outImg3 = uploadImage(transactionDTO.getOutImg3(), "outImg3");

        // 출차 정보 업데이트
        transaction.setOutImg1(outImg1);
        transaction.setOutImg2(outImg2);
        transaction.setOutImg3(outImg3);
        transaction.setExitTime(transactionDTO.getExitTime());
        transaction.setExitWeight(transactionDTO.getExitWeight());
        transaction.setUpdatedAt(LocalDateTime.now());

        // 저장 후 반환
        return transactionRepository.save(transaction);
    }



    // 입차 중량
    public Transaction entryWeight(Transaction transaction) {
        return transactionRepository.save(transaction); // 저장
    }

    // 출차 중량
    public Transaction exitWeight(Transaction transaction) {
        return transactionRepository.save(transaction); // 저장
    }

//    // 거래 정보
//    public TransactionService(TransactionRepository transactionRepository, ScrapPriceRepository scrapPriceRepository) {
//        this.transactionRepository = transactionRepository;
//        this.scrapPriceRepository = scrapPriceRepository;
//    }

    // 모든 거래 조회
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll(); // 모든 거래 데이터 조회
    }


//    public Transaction putExitDate(String carNumber, BigDecimal exitWeight) {
//        // 출차되지 않은 가장 최근의 거래 가져오기
//        Optional<Transaction> transOpt = transactionRepository.findLatestUnExitedEntryByCarNumber(carNumber);
//
//        if (transOpt.isEmpty()) {
//            throw new IllegalStateException("해당 차량의 진행 중인 거래가 없습니다.");
//        }
//
//        Transaction trans = transOpt.get();
//        trans.setExitWeight(exitWeight);
//        trans.setExitDate(LocalDateTime.now());
//
//        // 업데이트된 트랜잭션 저장
//        return transactionRepository.save(trans);
//    }

    // 고철 중량, 거래 총액 구하는 기능
    public Transaction getScrapTotalWeight(BigDecimal exitWeight, String carNumber, ScrapType scrapType) {

        // carNumber(차량 번호)를 기준으로 거래(Transaction)를 조회합니다.
        Transaction transaction = transactionRepository.findByCarNumber(carNumber);

        // 거래의 입고 중량(entryWeight)을 가져옵니다.
        BigDecimal entryWeight = transaction.getEntryWeight();

        // 입고 중량에서 출고 중량(exitWeight)을 빼서 스크랩 총 중량(totalWeight)을 계산합니다.
        BigDecimal totalWeight = entryWeight.subtract(exitWeight);

        // 계산된 총 중량을 거래 객체에 저장합니다.
        transaction.setTotalWeight(totalWeight);

        // scrapType(스크랩 종류)에 해당하는 가격 정보를 조회합니다.
        ScrapPrice scrapTypePrice = scrapPriceRepository.findPriceByScrapType(scrapType);

        // 총 중량과 스크랩 가격을 곱하여 구매 금액(purchaseAmount)을 계산합니다.
        BigDecimal purchaseAmount = totalWeight.multiply(scrapTypePrice.getPrice());

        // 계산된 구매 금액을 거래 객체에 저장합니다.
        transaction.setPurchaseAmount(purchaseAmount);

        // 업데이트된 거래 정보를 데이터베이스에 저장한 후 반환합니다.
        return transactionRepository.save(transaction);
    }

}
