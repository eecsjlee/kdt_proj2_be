package com.kdt_proj2_be.persistence;

import com.kdt_proj2_be.domain.ScrapMetalType;
import com.kdt_proj2_be.domain.ScrapType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScrapTypeRepository extends JpaRepository<ScrapType, Long> {

    Optional<ScrapType> findByScrapType(ScrapMetalType scrapMetalType);
}