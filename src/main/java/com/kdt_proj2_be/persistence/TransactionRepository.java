package com.kdt_proj2_be.persistence;

import com.kdt_proj2_be.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findByCarNumber(String carNumber);


//
//    @Query("SELECT t FROM Transaction t WHERE t.carNumber = :carNumber AND t.exitDate IS NULL ORDER BY t.entryDate DESC LIMIT 1")
//    Optional<Transaction> findLatestUnExitedEntryByCarNumber(@Param("carNumber") String carNumber);

}
