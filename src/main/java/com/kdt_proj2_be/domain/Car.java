package com.kdt_proj2_be.domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "car")
public class Car {

    @Id
    @Column(name = "car_number")
    private String carNumber;

    @ManyToOne
    @JoinColumn(name ="brn", nullable = false) //외래 키 매핑
    private Member member;

}
