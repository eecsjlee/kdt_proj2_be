package com.kdt_proj2_be.service;

import com.kdt_proj2_be.dto.HomeViewDTO;
import com.kdt_proj2_be.persistence.HomeViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomeViewService {

    private final HomeViewRepository homeViewRepository;

    public List<HomeViewDTO> getHomeViewData() {
        List<Object[]> results = homeViewRepository.findAllHomeView();

        return results.stream().map(row -> new HomeViewDTO(
                (String) row[0], // transaction_status
                (String) row[1], // car_number
                (String) row[2], // member_name
                (String) row[3], // member_contact
                (String) row[4], // scrap_type
                (BigDecimal) row[5], // total_weight
                (BigDecimal) row[6], // purchase_amount
                (BigDecimal) row[9], // price
                convertToLocalDateTime(row[7]), // entry_time
                convertToLocalDateTime (row[8]) // exit_time
        )).collect(Collectors.toList());
    }

    // Timestamp → LocalDateTime 변환 메서드
    private LocalDateTime convertToLocalDateTime(Object obj) {
        if (obj instanceof Timestamp) {
            return ((Timestamp) obj).toLocalDateTime();
        }
        return null;
    }

}