package com.kdt_proj2_be.controller;


import com.kdt_proj2_be.domain.Car;
import com.kdt_proj2_be.domain.Member;
import com.kdt_proj2_be.service.CarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/car")
public class CarController {

    private final CarService carService;

    //차량등록
    @PostMapping
    public Car registerCar(@RequestBody Car car) {
        return carService.registerCar(car);
    }

    @GetMapping
    public ResponseEntity<Car> getUserData(Authentication authentication) {
        return carService.getCarData(authentication);
    }

}
