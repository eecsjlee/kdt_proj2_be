package com.kdt_proj2_be.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kdt_proj2_be.domain.RequestStatus;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter @ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarDTO {

    private String carNumber; //차량 번호
    private String brn;
//    private String member; // 회원 정보
    private RequestStatus requestStatus; // 승인 상태 (enum 타입)
    private MultipartFile image; // 이미지 업로드 필드

}
