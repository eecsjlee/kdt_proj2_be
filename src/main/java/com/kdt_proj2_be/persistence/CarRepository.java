package com.kdt_proj2_be.persistence;

import com.kdt_proj2_be.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, String> {
    // 차량 번호 중복 체크 매서드 추가
    Optional<Car> findByCarNumber(String carNumber);
}
