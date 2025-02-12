package com.kdt_proj2_be.persistence;

import com.kdt_proj2_be.domain.ScrapType;
import com.kdt_proj2_be.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findFirstByCarNumberOrderByEntryTimeDesc(String carNumber);

    Optional<Transaction> findFirstByCarNumberAndExitTimeIsNullOrderByEntryTimeDesc(String carNumber);

    // 특정 ScrapType을 가진 거래 조회 (가격 업데이트 시 사용)
    List<Transaction> findByScrapType(ScrapType scrapType);
}
