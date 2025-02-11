package com.kdt_proj2_be.controller;

import com.kdt_proj2_be.dto.MissingRecordDTO;
import com.kdt_proj2_be.service.MissingRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 입차 기록이 없이 출차하는 차량을 조회하는 컨트롤러
 */
@Tag(name = "예외 처리 기록 API", description = "입차 기록이 없는 차량의 데이터를 조회하는 API")
@RestController
@RequestMapping("/records")
@RequiredArgsConstructor
public class MissingRecordController {

    private final MissingRecordService missingRecordService;

    /**
     * 모든 누락된 입차 기록 조회
     * @return List<MissingRecordDTO> - 모든 `MissingRecord` 데이터를 JSON 형태로 반환
     */
    @Operation(summary = "모든 예외 처리 기록 조회", description = "입차 기록이 없는 차량 목록을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 데이터를 반환함")
    @GetMapping
    public ResponseEntity<List<MissingRecordDTO>> getAllMissingRecords() {
        return ResponseEntity.ok(missingRecordService.getAllMissingRecords());
    }

    /**
     * 특정 차량의 누락된 입차 기록 조회
     *
     * 차량 번호(`carNumber`)를 입력하면 해당 차량의 누락된 입차 기록을 조회합니다.
     * 데이터가 없으면 404 에러를 반환합니다.
     *
     * @param carNumber - 조회할 차량 번호
     * @return MissingRecordDTO - 특정 차량의 `MissingRecord` 데이터를 반환
     */
    @Operation(summary = "특정 차량의 누락된 입차 기록 조회", description = "특정 차량 번호를 입력하면 해당 차량의 누락된 입차 기록을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 데이터를 반환함")
    @ApiResponse(responseCode = "404", description = "해당 차량의 입차 기록이 존재하지 않음")
    @GetMapping("/{carNumber}")
    public ResponseEntity<MissingRecordDTO> getMissingRecordByCarNumber(@PathVariable String carNumber) {
        return ResponseEntity.ok(missingRecordService.getMissingRecordByCarNumber(carNumber));
    }
}