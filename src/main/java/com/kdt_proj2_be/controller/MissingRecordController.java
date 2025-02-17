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
}