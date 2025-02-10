package com.kdt_proj2_be.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "scrap_type", nullable = false)
    private ScrapMetalType scrapType;

    @OneToMany(mappedBy = "scrapType")
    @JsonIgnore // ScrapType 객체를 JSON으로 변환할 때 prices 필드 직렬화를 막음
    private List<ScrapPrice> prices;
}