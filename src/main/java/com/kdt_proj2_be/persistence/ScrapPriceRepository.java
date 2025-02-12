package com.kdt_proj2_be.persistence;

import com.kdt_proj2_be.domain.ScrapMetalType;
import com.kdt_proj2_be.domain.ScrapPrice;
import com.kdt_proj2_be.domain.ScrapType;
import com.kdt_proj2_be.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScrapPriceRepository extends JpaRepository<ScrapPrice, Integer> {

    // 특정 ScrapMetalType과 effectiveDate가 같은 ScrapPrice 가져오기 (업데이트 확인용)
    Optional<ScrapPrice> findByScrapType_ScrapTypeAndEffectiveDate(ScrapMetalType scrapMetalType, LocalDateTime effectiveDate);

    // 특정 ScrapType을 가진 거래 조회 (가격 업데이트 시 사용)
    List<Transaction> findByScrapType(ScrapType scrapType);

    // 시작일과 종료일 사이의 ScrapPrice 데이터를 가져옵니다.
    List<ScrapPrice> findByEffectiveDateBetween(LocalDateTime start, LocalDateTime end);


    ScrapPrice findPriceByScrapType(ScrapType scrapType);

    @Query("SELECT sp FROM ScrapPrice sp WHERE sp.scrapType = :scrapType AND sp.effectiveDate <= :entryTime ORDER BY sp.effectiveDate DESC LIMIT 1")
    Optional<ScrapPrice> findLatestPriceBeforeEntryTime(@Param("scrapType") ScrapType scrapType, @Param("entryTime") LocalDateTime entryTime);



}