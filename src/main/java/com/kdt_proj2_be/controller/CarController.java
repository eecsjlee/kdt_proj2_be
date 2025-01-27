package com.kdt_proj2_be.controller;


import com.kdt_proj2_be.domain.Car;
import com.kdt_proj2_be.domain.Member;
import com.kdt_proj2_be.domain.RequestStatus;
import com.kdt_proj2_be.dto.CarDTO;
import com.kdt_proj2_be.service.CarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/car")
public class CarController {

    private final CarService carService;

    //차량등록
    @PostMapping
    public Car registerCar(@RequestBody CarDTO carDTO) {
        log.info("Registering Car with DTO: {}", carDTO); // 확인
        return carService.registerCar(carDTO);
    }

    @GetMapping
    public ResponseEntity<Car> getUserData(Authentication authentication) {
        return carService.getCarData(authentication);
    }

    @PutMapping
    public Car updateCar(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("carNumber") String carNumber,
            @RequestParam("member") String member,
            @RequestParam("requestStatus") RequestStatus requestStatus) throws IOException {

        // DTO 생성 및 설정
        CarDTO carDTO = CarDTO.builder()
                .carNumber(carNumber)
                .member(member)
                .requestStatus(requestStatus)
                .image(file)
                .build();

        // CarService 호출로 업데이트 처리
        return carService.updateCar(carDTO);
    }
}
