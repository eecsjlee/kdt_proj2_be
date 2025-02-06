package com.kdt_proj2_be.persistence;

import com.kdt_proj2_be.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {



//    // TODO: 한번 더 쿼리를 만줘 져야함
//    @Query("SELECT t FROM Transaction t WHERE t.licensePlate1 = :carNumber AND t.exitDate IS NULL ORDER BY t.entryDate DESC LIMIT 1")
//    Optional<Transaction> findLatestUnExitedEntryByCarNumber(@Param("carNumber") String carNumber);

}
