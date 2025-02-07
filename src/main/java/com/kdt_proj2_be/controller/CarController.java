package com.kdt_proj2_be.controller;


import com.kdt_proj2_be.domain.Car;
import com.kdt_proj2_be.domain.Member;
import com.kdt_proj2_be.domain.RequestStatus;
import com.kdt_proj2_be.dto.CarDTO;
import com.kdt_proj2_be.service.CarService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "car API", description = "차량 등록을 관리하는 API")
@RequestMapping("/car")
public class CarController {

    private final CarService carService;

    //차량등록
    @PostMapping
    public Car registerCar(
            @RequestParam(name = "numberPlate", required = false) MultipartFile file,
            @RequestParam("carNumber") String carNumber,
            @RequestParam("brn") String brn,
            @RequestParam(name = "requestStatus", defaultValue = "PENDING") RequestStatus requestStatus, InputStream inputStream) throws IOException {

        // DTO 생성 및 설정
        CarDTO carDTO = CarDTO.builder()
                .carNumber(carNumber)
                .brn(brn)
                .requestStatus(requestStatus)
                .image(file)
                .build();

        // CarService 호출로 업데이트 처리
        return carService.registerCar(carDTO);
    }

//    @PostMapping
//    public Car registerCar(@RequestBody CarDTO carDTO) throws IOException {
//        log.info("Registering Car with DTO: {}", carDTO); // 확인
//        return carService.registerCar(carDTO);
//    }

    @GetMapping
    public ResponseEntity<Car> getUserData(Authentication authentication) {
        return carService.getCarData(authentication);
    }

    @PatchMapping("/{carNumber}/approved")
    public ResponseEntity<String> approvedCar(@PathVariable String carNumber) throws IOException {

        Car car = Car.builder()
                .carNumber(carNumber)
                .build();

        carService.approvedCar(car);

        return ResponseEntity.ok("car has been approved successfully.");
    }

}
