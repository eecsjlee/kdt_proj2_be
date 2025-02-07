package com.kdt_proj2_be.service;

import com.kdt_proj2_be.domain.Transaction;
import com.kdt_proj2_be.dto.TransactionDTO;
import com.kdt_proj2_be.persistence.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

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

        // Transaction 엔티티 생성
        Transaction transaction = Transaction.builder()
//                .transactionStatus(transactionDTO.getTransactionStatus())
//                .scrapWeight(transactionDTO.getScrapWeight())
//                .purchaseAmount(transactionDTO.getPurchaseAmount())
//                .entryWeight(transactionDTO.getEntryWeight())
//                .exitWeight(transactionDTO.getExitWeight())
                .carNumber(transactionDTO.getCarNumber())
                .entryTime(transactionDTO.getEntryTime())
//                .exitTime(transactionDTO.getExitTime())
                .updatedAt(LocalDateTime.now()) // 업데이트 시간 설정
                .build();

        // Upload images and set them in the transaction
        String inImg1 = uploadImage(transactionDTO.getInImg1(), "inImg1");
        String inImg2 = uploadImage(transactionDTO.getInImg2(), "inImg2");
        String inImg3 = uploadImage(transactionDTO.getInImg3(), "inImg3");

        transaction.setInImg1(inImg1);
        transaction.setInImg2(inImg2);
        transaction.setInImg3(inImg3);

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

    // 거래 정보
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

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

}
