package com.kdt_proj2_be.service;

import com.kdt_proj2_be.dto.MissingRecordDTO;
import com.kdt_proj2_be.persistence.MissingRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissingRecordService {

    private final MissingRecordRepository missingRecordRepository;

    // 모든 MissingRecord 데이터 조회
    public List<MissingRecordDTO> getAllMissingRecords() {
        return missingRecordRepository.findAll().stream().map(missingRecord ->
                new MissingRecordDTO(
                        missingRecord.getCarNumber(),
                        missingRecord.getCheckedAt(),
                        missingRecord.getExitTime(),
                        missingRecord.getExitWeight()
                )
        ).collect(Collectors.toList());
    }

    // 특정 차량(carNumber)에 대한 누락된 입차 기록 조회
    public MissingRecordDTO getMissingRecordByCarNumber(String carNumber) {
        return missingRecordRepository.findByCarNumber(carNumber)
                .map(missingRecord -> new MissingRecordDTO(
                        missingRecord.getCarNumber(),
                        missingRecord.getCheckedAt(),
                        missingRecord.getExitTime(),
                        missingRecord.getExitWeight()
                ))
                .orElseThrow(() -> new RuntimeException("해당 차량의 MissingRecord 기록을 찾을 수 없습니다: " + carNumber));
    }
}