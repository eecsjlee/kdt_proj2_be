package com.kdt_proj2_be.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;


@Entity @Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "scrap_type")
public class ScrapType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scrapTypeId;

    private String scrapType;

    @OneToMany(mappedBy = "scrapType")
    private List<ScrapPrice> prices;

    public BigDecimal getLatestPrice() {
        return prices.stream()
                .max(Comparator.comparing(ScrapPrice::getEffectiveDate)) // 가장 최근 가격 찾기
                .map(ScrapPrice::getPrice)
                .orElse(BigDecimal.ZERO);
    }

}
