package com.kdt_proj2_be.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class ImageUtil {

    // 이미지 파일을 Base64로 변환
    public static String encodeImageToBase64(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return null; // 이미지가 없으면 null 반환
        }

        File file = new File("src/main/resources/static/images/" + imagePath);
        if (!file.exists()) {
            return null; // 파일이 존재하지 않으면 null 반환
        }

        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            return Base64.getEncoder().encodeToString(fileContent);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null; // 예외 발생 시 null 반환
        }
    }
}
