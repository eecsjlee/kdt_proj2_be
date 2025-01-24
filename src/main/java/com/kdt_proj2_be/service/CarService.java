package com.kdt_proj2_be.service;

import com.kdt_proj2_be.domain.Car;
import com.kdt_proj2_be.domain.Member;
import com.kdt_proj2_be.persistence.CarRepository;
import com.kdt_proj2_be.persistence.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final MemberRepository memberRepository;

    // 차량 등록
    public Car registerCar(Car car) {

        Member member = memberRepository.findByBrn(car.getMember().getBrn())
                .orElseThrow(() -> new RuntimeException("Member not found with BRN: " + car.getMember().getBrn()));

        // brn에 해당하는 Member 설정
        car.setMember(member);

        return carRepository.save(car); //차량정보를 DB에 저장
    }

    // 차량 중복 체크
    public boolean findCar(String carNumber) {
        return carRepository.findByCarNumber(carNumber).isPresent();
    }

//    //차량 중복 여부 확인
//    public boolean findCar(String carNumber) {
//        Member findCar = carRepository.findByCarNumber(carNumber).orElse(null);
//        return findCar != null ? true : false;
//    }

    // 로그인 후 회원 정보 전달
    public ResponseEntity<Car> getCarData(Authentication authentication) {
        String carNumber = authentication.getName(); // getName()이 맞는지?
        Car car = carRepository.findByCarNumber(carNumber).orElse(null);
        if (car != null) {
            return ResponseEntity.ok(car);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}
