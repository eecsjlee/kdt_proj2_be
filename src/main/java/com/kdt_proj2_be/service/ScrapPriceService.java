package com.kdt_proj2_be.service;

import com.kdt_proj2_be.domain.ScrapMetalType;
import com.kdt_proj2_be.domain.ScrapPrice;
import com.kdt_proj2_be.domain.ScrapType;
import com.kdt_proj2_be.dto.ScrapPriceRequestDTO;
import com.kdt_proj2_be.dto.ScrapPriceResponseDTO;
import com.kdt_proj2_be.persistence.ScrapPriceRepository;
import com.kdt_proj2_be.persistence.ScrapTypeRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScrapPriceService {

    private final ScrapPriceRepository scrapPriceRepository;
    private final ScrapTypeRepository scrapTypeRepository; // ScrapType 조회를 위한 Repository

    public ScrapPriceService(ScrapPriceRepository scrapPriceRepository, ScrapTypeRepository scrapTypeRepository) {
        this.scrapPriceRepository = scrapPriceRepository;
        this.scrapTypeRepository = scrapTypeRepository;
    }

    public List<ScrapPriceResponseDTO> getScrapPriceList() {

        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(29);

        List<ScrapPrice> scrapPrices = scrapPriceRepository.findByEffectiveDateBetween(startDate, endDate);
        Map<LocalDate, List<ScrapPrice>> groupedByDate = scrapPrices.stream()
                .collect(Collectors.groupingBy(sp -> sp.getEffectiveDate().toLocalDate()));

        List<ScrapPriceResponseDTO> responses = new ArrayList<>();
        for(Map.Entry<LocalDate, List<ScrapPrice>> entry : groupedByDate.entrySet()) {
            LocalDate date = entry.getKey();
            List<ScrapPrice> dailyPrices = entry.getValue();

            Map<String, BigDecimal> pricesMap = new HashMap<>();
            for(ScrapMetalType type : ScrapMetalType.values()) {
                Optional<ScrapPrice> scrapPriceOptional = dailyPrices.stream()
                        .filter(sp -> sp.getScrapType().getScrapType() == type)
                        .findFirst();
                pricesMap.put(type.name(), scrapPriceOptional.map(ScrapPrice::getPrice).orElse(BigDecimal.ZERO));
            }
            responses.add(new ScrapPriceResponseDTO(date, pricesMap));
        }
        responses.sort(Comparator.comparing(ScrapPriceResponseDTO::getEffectiveDate));

        return responses;
    }

//    // 고철 가격 정보를  DB에 저장
//    public ScrapPrice registerPrice(ScrapPriceRequestDTO requestDTO) {
//
//        // ENUM 값을 통해 ScrapType 찾기
//        ScrapType scrapType = scrapTypeRepository.findByScrapType(requestDTO.getScrapType())
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 고철 종류입니다."));
//
//        // 엔티티 변환
//        ScrapPrice scrapPrice = new ScrapPrice();
//        scrapPrice.setScrapType(scrapType);
//        scrapPrice.setPrice(requestDTO.getPrice());
//        scrapPrice.setEffectiveDate(requestDTO.getEffectiveDate());
//
//        // 저장 후 반환
//        return scrapPriceRepository.save(scrapPrice);
//    }

    public void registerPrices(ScrapPriceRequestDTO requestDTO) {
        // 날짜 변환: LocalDate → LocalDateTime (시간을 00:00:00으로 설정)
        LocalDateTime effectiveDateTime = requestDTO.getEffectiveDate().atStartOfDay();

        // 요청된 모든 고철 가격을 저장
        for (Map.Entry<ScrapMetalType, BigDecimal> entry : requestDTO.getPrices().entrySet()) {
            ScrapMetalType scrapTypeEnum = entry.getKey();
            BigDecimal price = entry.getValue();

            // ScrapType 조회
            ScrapType scrapType = scrapTypeRepository.findByScrapType(scrapTypeEnum)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 고철 종류: " + scrapTypeEnum));

            // ScrapPrice 엔티티 생성 및 저장
            ScrapPrice scrapPrice = new ScrapPrice();
            scrapPrice.setScrapType(scrapType);
            scrapPrice.setPrice(price);
            scrapPrice.setEffectiveDate(effectiveDateTime);

            scrapPriceRepository.save(scrapPrice);
        }
    }

}
