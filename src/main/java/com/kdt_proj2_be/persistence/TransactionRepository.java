package com.kdt_proj2_be.persistence;

import com.kdt_proj2_be.domain.ScrapType;
import com.kdt_proj2_be.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findByCarNumber(String carNumber);

    // 차번호 중 가장 최근 입차한 차량을 찾음
    @Query("SELECT t FROM Transaction t WHERE t.carNumber = :carNumber ORDER BY t.entryTime DESC LIMIT 1")
    Optional<Transaction> findLatestTransactionByCarNumber(@Param("carNumber") String carNumber);

    Optional<Transaction> findFirstByCarNumberOrderByEntryTimeDesc(String carNumber);

    Optional<Transaction> findFirstByCarNumberAndExitTimeIsNullOrderByEntryTimeDesc(String carNumber);

    // 특정 ScrapType을 가진 거래 조회 (가격 업데이트 시 사용)
    List<Transaction> findByScrapType(ScrapType scrapType);


}
