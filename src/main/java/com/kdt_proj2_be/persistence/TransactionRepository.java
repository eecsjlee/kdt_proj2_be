package com.kdt_proj2_be.persistence;

import com.kdt_proj2_be.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {


}
