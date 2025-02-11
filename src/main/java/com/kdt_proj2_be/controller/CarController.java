package com.kdt_proj2_be.controller;

import com.kdt_proj2_be.domain.Car;
import com.kdt_proj2_be.domain.RequestStatus;
import com.kdt_proj2_be.dto.CarDTO;
import com.kdt_proj2_be.service.CarService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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

    // 모든 차량 정보를 JSON으로 반환
    @GetMapping
    public ResponseEntity<List<CarDTO>> getAllCars() {
        List<CarDTO> cars = carService.getAllCars();
        return ResponseEntity.ok(cars);
    }

    // 특정 차량 번호로 차량 정보를 JSON으로 반환
    @GetMapping("/{carNumber}")
    public ResponseEntity<CarDTO> getCarByNumber(@PathVariable String carNumber) {
        CarDTO carDTO = carService.getCarByNumber(carNumber);
        return ResponseEntity.ok(carDTO);
    }

    @PutMapping("/{carNumber}/approved")
    public ResponseEntity<String> approvedCar(@PathVariable String carNumber) throws IOException {

        Car car = Car.builder()
                .carNumber(carNumber)
                .build();

        carService.approvedCar(car);

        return ResponseEntity.ok("car has been approved successfully.");
    }

    // 차량 상태 일괄 변경 (PENDING → APPROVED / REJECTED)
    @PutMapping("/status")
    public ResponseEntity<List<CarDTO>> updateCarStatuses(@RequestBody List<CarDTO> carDTOList) {
        List<CarDTO> updatedCars = carService.updateCarStatuses(carDTOList);
        return ResponseEntity.ok(updatedCars);
    }
}
