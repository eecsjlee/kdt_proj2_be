package com.kdt_proj2_be.service;

import com.kdt_proj2_be.domain.ScrapMetalType;
import com.kdt_proj2_be.domain.ScrapPrice;
import com.kdt_proj2_be.domain.ScrapType;
import com.kdt_proj2_be.dto.ScrapPriceRequestDTO;
import com.kdt_proj2_be.dto.ScrapPriceResponseDTO;
import com.kdt_proj2_be.persistence.ScrapPriceRepository;
import com.kdt_proj2_be.persistence.ScrapTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScrapPriceService {

    private final ScrapPriceRepository scrapPriceRepository;
    private final ScrapTypeRepository scrapTypeRepository; // ScrapType 조회를 위한 Repository

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

//    public void registerPrices(ScrapPriceRequestDTO requestDTO) {
//
//        // 날짜 변환: LocalDate → LocalDateTime (시간을 00:00:00으로 설정)
//        LocalDateTime effectiveDateTime = requestDTO.getEffectiveDate().atStartOfDay();
//
//        // 요청된 모든 고철 가격을 저장
//        for (Map.Entry<ScrapMetalType, BigDecimal> entry : requestDTO.getPrices().entrySet()) {
//            ScrapMetalType scrapTypeEnum = entry.getKey();
//            BigDecimal price = entry.getValue();
//
//            // ScrapType 조회
//            ScrapType scrapType = scrapTypeRepository.findByScrapType(scrapTypeEnum)
//                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 고철 종류: " + scrapTypeEnum));
//
//            // ✅ 기존에 같은 날짜(effectiveDate)와 같은 고철(scrapType) 데이터가 있는지 확인
//            Optional<ScrapPrice> existingPrice = scrapPriceRepository.findByScrapType_ScrapTypeAndEffectiveDate(scrapTypeEnum, effectiveDateTime);
//
//            if (existingPrice.isPresent()) {
//                // ✅ 기존 데이터가 있으면 가격을 업데이트
//                ScrapPrice scrapPrice = existingPrice.get();
//                scrapPrice.setPrice(price);
//                scrapPriceRepository.save(scrapPrice); // 업데이트 수행
//            }
//            else {
//                // 기존 데이터가 없으면 새로운 데이터 추가
//                ScrapPrice scrapPrice = new ScrapPrice();
//                scrapPrice.setScrapType(scrapType);
//                scrapPrice.setPrice(price);
//                scrapPrice.setEffectiveDate(effectiveDateTime);
//                scrapPriceRepository.save(scrapPrice);
//            }
//
////            // ScrapPrice 엔티티 생성 및 저장
////            ScrapPrice scrapPrice = new ScrapPrice();
////            scrapPrice.setScrapType(scrapType);
////            scrapPrice.setPrice(price);
////            scrapPrice.setEffectiveDate(effectiveDateTime);
////
////            scrapPriceRepository.save(scrapPrice);
//        }
//    }

    // 같은 날짜와 같은 고철이면 기존 데이터를 덮어쓰는 로직 추가
    public void registerPrices(ScrapPriceRequestDTO requestDTO) {

        // 날짜 변환: LocalDate → LocalDateTime (시간을 00:00:00으로 설정)
        LocalDateTime effectiveDateTime = requestDTO.getEffectiveDate().atStartOfDay();

        // 요청된 모든 고철 가격을 저장 (덮어쓰기 로직 추가)
        for (Map.Entry<ScrapMetalType, BigDecimal> entry : requestDTO.getPrices().entrySet()) {
            ScrapMetalType scrapTypeEnum = entry.getKey();
            BigDecimal price = entry.getValue();

            // ScrapType 조회
            ScrapType scrapType = scrapTypeRepository.findByScrapType(scrapTypeEnum)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 고철 종류: " + scrapTypeEnum));

            // 기존에 같은 날짜(effectiveDate)와 같은 고철(scrapType) 데이터가 있는지 확인
            Optional<ScrapPrice> existingPrice = scrapPriceRepository.findByScrapType_ScrapTypeAndEffectiveDate(scrapTypeEnum, effectiveDateTime);

            if (existingPrice.isPresent()) {
                // 기존 데이터가 있으면 가격을 업데이트
                ScrapPrice scrapPrice = existingPrice.get();
                scrapPrice.setPrice(price);
                scrapPriceRepository.save(scrapPrice); // 업데이트 수행
            }
            else {
                // 기존 데이터가 없으면 새로운 데이터 추가
                ScrapPrice scrapPrice = new ScrapPrice();
                scrapPrice.setScrapType(scrapType);
                scrapPrice.setPrice(price);
                scrapPrice.setEffectiveDate(effectiveDateTime);
                scrapPriceRepository.save(scrapPrice);
            }
        }
    }
}
