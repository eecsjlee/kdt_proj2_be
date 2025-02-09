package com.kdt_proj2_be.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "scrap_price")
public class ScrapPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scrapPriceId;

    @ManyToOne
    @JoinColumn(name = "scrap_type_id")
    private ScrapType scrapType;

    private BigDecimal price;
    private LocalDateTime effectiveDate;
}
