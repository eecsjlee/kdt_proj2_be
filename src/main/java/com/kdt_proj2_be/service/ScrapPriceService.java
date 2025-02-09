package com.kdt_proj2_be.service;

import com.kdt_proj2_be.domain.ScrapMetalType;
import com.kdt_proj2_be.domain.ScrapPrice;
import com.kdt_proj2_be.dto.ScrapPriceResponseDTO;
import com.kdt_proj2_be.persistence.ScrapPriceRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScrapPriceService {

    private final ScrapPriceRepository scrapPriceRepository;

    public ScrapPriceService(ScrapPriceRepository scrapPriceRepository) {
        this.scrapPriceRepository = scrapPriceRepository;
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

    public updatePrices

}
