package com.kdt_proj2_be.controller;

import com.kdt_proj2_be.domain.Member;
import com.kdt_proj2_be.domain.ScrapPrice;
import com.kdt_proj2_be.dto.ScrapPriceRequestDTO;
import com.kdt_proj2_be.dto.ScrapPriceResponseDTO;
import com.kdt_proj2_be.service.ScrapPriceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ScrapPriceController는 고철 가격 정보를 제공하는 REST 컨트롤러입니다.
 */
@Tag(name = "Scrap Price", description = "고철 가격 API") // Swagger 그룹 태그 설정
@RestController
@RequestMapping("/scraps") // 기본 URL 경로를 "/scraps"로 설정
public class ScrapPriceController {

    // 고철 가격 관련 서비스
    private final ScrapPriceService scrapPriceService;

    /**
     * ScrapPriceController 생성자
     *
     * @param scrapPriceService 스크랩 가격 서비스 주입
     */
    public ScrapPriceController(ScrapPriceService scrapPriceService) {
        this.scrapPriceService = scrapPriceService;
    }


    /**
     * 스크랩 가격 목록을 조회하는 엔드포인트
     *
     * @return 스크랩 가격 목록을 포함하는 ResponseEntity
     */
    @Operation(summary = "고철 가격 조회", description = "고철 가격 목록을 조회합니다.")
    @GetMapping("/prices") // GET 요청을 "/scraps/prices" 경로로 매핑
    public ResponseEntity<List<ScrapPriceResponseDTO>> prices(){

        // 서비스에서 가격 목록 가져오기
        List<ScrapPriceResponseDTO> response = scrapPriceService.getScrapPriceList();

        // 200 OK 응답 반환
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "고철 가격 입력", description = "여러 고철 가격을 한 번에 입력합니다.")
    @PostMapping("/prices")
    public ResponseEntity<String> registerPrices(@RequestBody ScrapPriceRequestDTO requestDTO) {

        // 서비스 계층에서 여러 고철 가격을 처리하도록 수정
        scrapPriceService.registerPrices(requestDTO);

        return ResponseEntity.ok("Scrap prices successfully saved!");
    }
}
