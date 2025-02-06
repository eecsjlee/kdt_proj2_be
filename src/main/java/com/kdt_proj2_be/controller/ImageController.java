package com.kdt_proj2_be.controller;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Slf4j // 로깅 기능 추가 (Lombok 사용)
@RequiredArgsConstructor
@RestController
public class ImageController {

    private final String pythonServerUrl = "http://10.125.121.214:5000/process_image";
    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/upload")
    public ResponseEntity<JsonNode> handleFileUpload(@RequestParam(value = "file", required = false) MultipartFile file,
                                                     @RequestParam(value = "result", required = false) String result) throws IOException {
        log.info("File received: {}", file != null ? file.getOriginalFilename() : "No File");
        log.info("Result received : {}", result);
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<JsonNode> response = restTemplate.exchange(pythonServerUrl, HttpMethod.POST, request, JsonNode.class);
        if(response.getStatusCode().is2xxSuccessful()) {
            return response;
        }
        else{
            return ResponseEntity.status(response.getStatusCode()).body(null);
        }
    }

    @PostMapping("/result")
    public ResponseEntity<JsonNode> handleResultUpload(@RequestParam(value = "file", required = false) MultipartFile file,
                                                       @RequestParam(value = "result", required = false) String result) throws IOException {
        log.info("File received from Python: {}", file != null ? file.getOriginalFilename() : "No File");
        log.info("Result received from Python: {}", result);
        log.info("file{}", file.toString());
        HttpHeaders headers = new HttpHeaders();
        if (file != null && !file.isEmpty()){
            headers.setContentType(MediaType.valueOf(file.getContentType()));
        }
        return ResponseEntity.ok().headers(headers).body(null);
    }
}