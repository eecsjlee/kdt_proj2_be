package com.kdt_proj2_be.persistence;

import com.kdt_proj2_be.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HomeViewRepository extends JpaRepository<Transaction, Long> {

    @Query(value = "SELECT * FROM home_view", nativeQuery = true) // SQL 사용
    List<Object[]> findAllHomeView();
}