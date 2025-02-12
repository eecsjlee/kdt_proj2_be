package com.kdt_proj2_be.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kdt_proj2_be.domain.RequestStatus;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter @ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarDTO {

    private String carNumber; //차량 번호
    private String brn; // Member 객체에서 brn을 가져옴
    private RequestStatus requestStatus; // 승인 상태 (enum 타입)

    @JsonIgnore
    private MultipartFile image; // 이미지 업로드 필드

    // Car 엔티티를 CarDTO로 변환하는 정적 메서드
    public static CarDTO fromEntity(com.kdt_proj2_be.domain.Car car) {
        return CarDTO.builder()
                .carNumber(car.getCarNumber())
                .brn(car.getMember().getBrn()) // Member 객체에서 brn 값 가져오기
                .requestStatus(car.getRequestStatus())
                .build();
    }
}
