package com.kdt_proj2_be.persistence;

import com.kdt_proj2_be.domain.MissingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MissingRecordRepository extends JpaRepository<MissingRecord, Long> {
    Optional<MissingRecord> findByCarNumber(String carNumber);
}