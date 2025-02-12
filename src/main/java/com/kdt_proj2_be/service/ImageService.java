package com.kdt_proj2_be.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.kdt_proj2_be.dto.TransactionDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
        import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Service @Getter @Setter
public class ImageService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String pythonServerUrl = "http://10.125.121.214:5000/process_image"; // Python 서버 URL

    /**
     * Base64 데이터를 MultipartFile로 변환하는 메서드
     */
    public MultipartFile convertBase64ToMultipartFile(String base64Data, String fileName) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64Data);
            return new MockMultipartFile(fileName, fileName, "image/jpeg", decodedBytes);
        } catch (IllegalArgumentException e) {
            log.error("Base64 디코딩 오류: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Python 서버로 이미지를 전송하는 메서드
     */
    public TransactionDTO sendImageToPythonServer(MultipartFile file) throws IOException {
        // HttpHeaders 객체를 생성하여 요청 헤더를 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // MultipartFile을 전송할 본문을 생성하기 위한 MultiValueMap
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        body.add("file", file.getResource());

        // HttpEntity 객체 생성: 헤더와 본문을 결합하여 요청 엔티티 구성
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

        // RestTemplate를 사용하여 Python 서버로 POST 요청 (응답을 무시하고 상태 코드만 확인)
        ResponseEntity<JsonNode> response = restTemplate.exchange(pythonServerUrl, HttpMethod.POST, request, JsonNode.class);

        // HTTP 상태 코드 확인
        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode responseBody = response.getBody();
            log.info("Python 서버 응답: {}", responseBody);

            // 예를 들어, "file" 키가 존재하는지 확인 (파이썬 서버가 이런 식으로 반환한다고 가정)
            TransactionDTO transactionDTO = null;

            if (responseBody != null) {

                // TransactionDTO를 빌더로 생성할 때 Python 서버의 값을 채워 넣습니다.
                transactionDTO = TransactionDTO.builder().build();

                if (responseBody.has("carNumber")) {
                    transactionDTO.setCarNumber(responseBody.get("carNumber").asText());
                } else {
                    log.warn("응답에 carNumber 정보가 없습니다.");
                }
                if (responseBody.has("inImg2")) {
                    String base64Data = responseBody.get("inImg2").asText();
                    MultipartFile inImg2File = convertBase64ToMultipartFile(base64Data, "inImg2.jpg");
                    transactionDTO.setInImg2(inImg2File);
                } else {
                    log.warn("응답에 inImg2 정보가 없습니다.");
                }

                if (responseBody.has("inImg3")) {
                    String base64Data = responseBody.get("inImg3").asText();
                    MultipartFile inImg3File = convertBase64ToMultipartFile(base64Data, "inImg3.jpg");
                    transactionDTO.setInImg3(inImg3File);
                } else {
                    log.warn("응답에 inImg3 정보가 없습니다.");
                }

                return transactionDTO;
            }
            else {
                log.warn("응답 본문이 null입니다.");
            }
        }
        else {
            log.error("Python 서버 에러 발생: {}", response.getStatusCode());
        }
        return null;
    }

    /**
     * 이미지 파일을 지정된 경로에 업로드하고 새 파일명을 반환하는 메서드
     */
    public String uploadImage(MultipartFile file, String prefix) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // 파일의 절대 경로 가져오기
        String absolutePath = new File("").getAbsolutePath() + File.separator;
        String path = "src/main/resources/static/images";
        File imgDir = new File(path);
        if (!imgDir.exists()) {
            imgDir.mkdirs();
        }

        String originalFileName = file.getOriginalFilename();
        int lastIndex = originalFileName.lastIndexOf('.') + 1;
        String ext = originalFileName.substring(lastIndex);

        // 시간 기반으로 파일 중복 회피
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String newFileName = sdf.format(new Date()) + "_" + prefix + "." + ext;

        // 파일 저장
        file.transferTo(new File(absolutePath + path + File.separator + newFileName));
        return newFileName;
    }
}