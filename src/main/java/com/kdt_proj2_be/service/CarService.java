package com.kdt_proj2_be.service;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import com.kdt_proj2_be.domain.Car;
import com.kdt_proj2_be.domain.Member;
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


@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final MemberRepository memberRepository;

    // 차량 등록
    public Car registerCar(CarDTO carDTO, String brn) throws IOException {

        // 이미지 업로드
        String imagePath = imageUpload(carDTO);

        // member 조회 FIXME 어디서 잘못 됐는지 모르겠다
        Member member = memberRepository.findByBrn(brn)
                .orElseThrow(() -> new RuntimeException("Member not found with BRN: " + carDTO.getCarNumber()));

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

//    // 사진 경로 저장
//    String userImgName = imageUpload(carDTO);
//
//    // Member 조회
//    Member member = memberRepository.findById(carDTO.getMember()).orElse(null);
//    if (member == null) {
//        return null; // 회원 정보가 없으면 null 반환
//    }

//    // Car 엔티티 저장
//    Car car = carRepository.save(Car.builder()
//            .carNumber(carDTO.getCarNumber()) // 차량 번호 설정
//            .member(member) // 회원 엔티티 설정
//            .requestStatus(carDTO.getRequestStatus()) // 승인 상태 설정
//            .build());
//
//    // 저장된 이미지 경로를 추가로 설정 (필요 시)
//    car.setImage(userImgName);
//    carRepository.save(car);
//
//    return car;


//    // 사진 등록
//    public String imageUpload(CarDTO carDTO) throws IOException {
//        MultipartFile file = carDTO.getImage();
//        String absolutePath = new File("").getAbsolutePath() + File.separator;
//        String path = "src/main/resources/static/images";
//        File imageDirectory = new File(path);
//        String imageName = null;
//
//        // 디렉토리 없으면 생성
//        if (!imageDirectory.exists()) imageDirectory.mkdirs();
//
//        // 파일이 null이거나 비어있으면 null 반환
//        if (file == null || file.isEmpty()) {
//            return null;
//        }
//
//        // 파일의 ContentType 확인 및 확장자 설정
//        String contentType = file.getContentType();
//        String originalFileExtension;
//
//        if (ObjectUtils.isEmpty(contentType)) {
//            return null;
//        } else if (contentType.contains("image/jpeg")) {
//            originalFileExtension = ".jpg";
//        } else if (contentType.contains("image/png")) {
//            originalFileExtension = ".png";
//        } else {
//            return null; // 허용되지 않은 확장자
//        }
//
//        // 파일 저장 이름 생성
//        String originalFileName = file.getOriginalFilename();
//        String fileName = (originalFileName != null && originalFileName.contains("."))
//                ? originalFileName.substring(0, originalFileName.lastIndexOf('.'))
//                : "default";
//
//        imageName = carDTO.getMember() + "_" + carDTO.getCarNumber() + originalFileExtension;
//
//        // 파일 저장
//        File destinationFile = new File(absolutePath + imageDirectoryPath + File.separator + imageName);
//        file.transferTo(destinationFile);
//
//        return imageName;
//    }

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
                // 타입 없으면 null
                return null;
            } else {
                if (contentType.contains("image/jpeg")) {
                    originalFileExtension = ".jpg";
                } else if (contentType.contains("image/png")) {
                    originalFileExtension = ".png";
                } else {
                    return null;
                }
            }

            // 파일저장 이름
            String originalFileName = file.getOriginalFilename();

            // 확장자를 제외한 파일 이름과 확장자 추출
            int lastIndex = originalFileName.lastIndexOf('.');
            String fileName = originalFileName.substring(0, lastIndex);
            userImgName = carDTO.getCarNumber() + "_" + carDTO.getCarNumber() + originalFileExtension;
            // 파일 저장
            userImg = new File(absolutePath  + path + File.separator + userImgName);
            System.out.println("파일 저장경로:" + absolutePath  + path + File.separator + userImgName);
            file.transferTo(userImg);
        }

        return userImgName;
    }

//        // 파일 저장
//        File destinationFile = new File(absolutePath + imageDirectoryPath + File.separator + imageName);
//        file.transferTo(destinationFile);
//
//        return imageName;
//    }
//
//    // 파일 확장자 추출
//    private String getFileExtension(String contentType) {
//
//        if (contentType == null) {
//            return null;
//        }
//
//        return switch (contentType) {
//            case "image/jpeg" -> ".jpg";
//            case "image/png" -> ".png";
//            case "image/gif" -> ".gif";
//            case "image/webp" -> ".webp";
//            default -> null;
//        };
//    }

    // 차량 정보 수정
    public Car updateCar(CarDTO carDTO) throws IOException {

        // 차량 번호를 기준으로 기존 차량 정보를 조회
        Car car = carRepository.findByCarNumber(carDTO.getCarNumber())
                .orElseThrow(() -> new RuntimeException("차량 정보를 찾을 수 없습니다. 차량 번호: " + carDTO.getCarNumber()));

        // 이미지 파일 처리
        if (carDTO.getImage() != null && !carDTO.getImage().isEmpty()) {
            // 기존 이미지 삭제(필요한 경우)
            if (car.getImage() != null) {
                File oldFile = new File("src/main/resources/static/images/" + car.getImage());
                if (oldFile.exists()) {
                    oldFile.delete();
                }
            }

            // 새 이미지 업로드
            String newImagePath = imageUpload(carDTO);
            car.setImage(newImagePath);
        }

        // 차량 상태 업데이트
        if (carDTO.getRequestStatus() != null) {
            car.setRequestStatus(carDTO.getRequestStatus());
        }

        // Member 정보 업데이트
        if (carDTO.getMember() != null) {
            Member member = memberRepository.findByBrn(carDTO.getMember())
                    .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다. 회원 BRN: " + carDTO.getMember()));
            car.setMember(member);
        }

        // 변경된 차량 정보 저장
        return carRepository.save(car);

    }
}
