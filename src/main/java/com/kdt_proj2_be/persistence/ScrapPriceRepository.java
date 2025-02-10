package com.kdt_proj2_be.persistence;

import com.kdt_proj2_be.domain.ScrapMetalType;
import com.kdt_proj2_be.domain.ScrapPrice;
import com.kdt_proj2_be.domain.ScrapType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ScrapPriceRepository extends JpaRepository<ScrapPrice, Integer> {

//    @Query("select sp.price " +
//            "from ScrapPrice sp " +
//            "where sp.scrapType.scrapType = :scrapMetalType " +
//            "order by sp.effectiveDate asc")
//    List<BigDecimal> findPricesByScrapMetalType(@Param("scrapMetalType") ScrapMetalType scrapMetalType);

    // 특정 ScrapMetalType과 effectiveDate에 해당하는 ScrapPrice를 가져옵니다.
    ScrapPrice findByScrapType_ScrapTypeAndEffectiveDate(ScrapMetalType scrapMetalType, LocalDateTime effectiveDate);

    // 시작일과 종료일 사이의 ScrapPrice 데이터를 가져옵니다.
    List<ScrapPrice> findByEffectiveDateBetween(LocalDateTime start, LocalDateTime end);


    ScrapPrice findPriceByScrapType(ScrapType scrapType);

}