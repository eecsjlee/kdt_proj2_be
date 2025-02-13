package com.kdt_proj2_be.persistence;

import com.kdt_proj2_be.domain.ScrapType;
import com.kdt_proj2_be.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findFirstByCarNumberOrderByEntryTimeDesc(String carNumber);

    Optional<Transaction> findFirstByCarNumberAndExitTimeIsNullOrderByEntryTimeDesc(String carNumber);

    // 특정 ScrapType을 가진 거래 조회 (가격 업데이트 시 사용)
    List<Transaction> findByScrapType(ScrapType scrapType);

    // updated_at을 내림차순으로 정렬하여 가져오는 쿼리
    @Query("SELECT t FROM Transaction t ORDER BY t.updatedAt DESC")
    List<Transaction> findAllByOrderByUpdatedAtDesc();
}
