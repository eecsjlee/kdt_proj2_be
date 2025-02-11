package com.kdt_proj2_be.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.kdt_proj2_be.domain.Car;
import com.kdt_proj2_be.domain.Member;
import com.kdt_proj2_be.domain.RequestStatus;
import com.kdt_proj2_be.dto.CarDTO;
import com.kdt_proj2_be.persistence.CarRepository;
import com.kdt_proj2_be.persistence.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.kdt_proj2_be.domain.RequestStatus.APPROVED;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final MemberRepository memberRepository;

    // 차량 등록
    public Car registerCar(CarDTO carDTO) throws IOException {

        // 이미지 업로드
        String imagePath = imageUpload(carDTO);

        // Member 조회 (brn을 기반으로 검색)
        Member member = memberRepository.findByBrn(carDTO.getBrn())
                .orElseThrow(() -> new RuntimeException("해당 사업자 번호(brn)를 찾을 수 없습니다: " + carDTO.getBrn()));
        // Car 엔티티 생성 및 저장
        Car car = Car.builder()
                .carNumber(carDTO.getCarNumber())
                .member(member)
                .requestStatus(carDTO.getRequestStatus())
                .image(imagePath) // 이미지 경로 설정
                .build();

        return carRepository.save(car);
    }

    // 차량 중복 체크
    public boolean isCarNumberDuplicate(String carNumber) {
        return carRepository.findByCarNumber(carNumber).isPresent();
    }

    //    //차량 중복 여부 확인
    //    public boolean findCar(String carNumber) {
    //        Member findCar = carRepository.findByCarNumber(carNumber).orElse(null);
    //        return findCar != null ? true : false;
    //    }


    // 로그인 후 차량 정보 조회
    public ResponseEntity<Car> getCarData(Authentication authentication) {
        String carNumber = authentication.getName(); // 차량 번호가 반환되는지 확인 필요
        Car car = carRepository.findByCarNumber(carNumber).orElse(null);

        if (car != null) {
            return ResponseEntity.ok(car);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 이미지 업로드 사진 등록
    public String imageUpload(CarDTO carDTO) throws IOException {
        MultipartFile file = carDTO.getImage();
        String absolutePath = new File("").getAbsolutePath() + File.separator;
        String path = "src/main/resources/static/images";
        File userImg = new File(path);
        String userImgName = null;

        if (!userImg.exists()) userImg.mkdirs();
        if (file == null) return null;
        if (!file.isEmpty()) {
            // 파일이 비어있지 않으면
            String contentType = file.getContentType();
            String originalFileExtension;

            // 타입에 따른 확장자 결정
            if (ObjectUtils.isEmpty(contentType)) {

                return null; // 타입 없으면 null
            }

            // 파일저장 이름
            String originalFileName = file.getOriginalFilename();

            int lastIndex = originalFileName.lastIndexOf('.') + 1;
            String fileName = originalFileName.substring(0, lastIndex-1);

            String ext = originalFileName.substring(lastIndex);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

            userImgName = carDTO.getCarNumber() + "_" + sdf.format(new Date()) +  "." + ext;

            // 파일 저장
            System.out.println("파일 저장경로:" + absolutePath  + path + File.separator + userImgName);
            file.transferTo(new File(absolutePath  + path + File.separator + userImgName));
        }
        return userImgName;
    }

    // 차량 승인
    public Car approvedCar(Car car) throws IOException {

        // 승인 처리
        car.setRequestStatus(RequestStatus.valueOf("APPROVED"));

        // 변경 사항 저장
        return carRepository.save(car);
    }


    // 차량 정보 수정
    public Car updateCar(CarDTO carDTO) throws IOException {

        // 차량 번호를 기준으로 기존 차량 정보를 조회
        Car car = carRepository.findByCarNumber(carDTO.getCarNumber())
                .orElseThrow(() -> new RuntimeException("차량 정보를 찾을 수 없습니다. 차량 번호: " + carDTO.getCarNumber()));

        // 이미지 파일 처리
        if (carDTO.getImage() != null && !carDTO.getImage().isEmpty()) {
            // 기존 이미지 삭제(필요한 경우)
            if (carDTO.getImage() != null) {
                File oldFile = new File("src/main/resources/static/images/" + carDTO.getImage());
                if (oldFile.exists()) {
                    oldFile.delete();
                }
            }
        }

        // 차량 상태 업데이트
        if (carDTO.getRequestStatus() != null) {
            carDTO.setRequestStatus(carDTO.getRequestStatus());
        }

        // 변경된 차량 정보 저장
        return carRepository.save(car);

    }




    // 모든 차량을 `CarDTO` 리스트로 변환하여 반환
    public List<CarDTO> getAllCars() {
        return carRepository.findAll().stream().map(car ->
                CarDTO.builder()
                        .carNumber(car.getCarNumber())
                        .brn(car.getMember().getBrn())
                        .requestStatus(car.getRequestStatus())
                        .build()
        ).toList();
    }

    // 특정 차량을 CarDTO로 변환하여 반환
    public CarDTO getCarByNumber(String carNumber) {
        Car car = carRepository.findByCarNumber(carNumber)
                .orElseThrow(() -> new RuntimeException("차량 정보를 찾을 수 없습니다. 차량 번호: " + carNumber));

        return CarDTO.builder()
                .carNumber(car.getCarNumber())
                .brn(car.getMember().getBrn())
                .requestStatus(car.getRequestStatus())
                .build();
    }




    // 여러 차량 상태 변경
    public List<CarDTO> updateCarStatuses(List<CarDTO> carDTOList) {
        List<CarDTO> updatedCars = carDTOList.stream().map(carDTO -> {
            Car car = carRepository.findByCarNumber(carDTO.getCarNumber())
                    .orElseThrow(() -> new RuntimeException("차량 정보를 찾을 수 없습니다: " + carDTO.getCarNumber()));

            // 🚗 `PENDING` 상태에서만 변경 가능
            if (car.getRequestStatus() == RequestStatus.PENDING) {
                car.setRequestStatus(carDTO.getRequestStatus()); // ✅ 상태 변경
            } else {
                throw new IllegalStateException("차량 상태 변경이 불가능합니다: " + carDTO.getCarNumber());
            }

            carRepository.save(car);

            return CarDTO.builder()
                    .carNumber(car.getCarNumber())
                    .brn(car.getMember().getBrn())
                    .requestStatus(car.getRequestStatus())
                    .build();
        }).toList();

        return updatedCars;
    }









}
