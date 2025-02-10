package com.kdt_proj2_be.dto;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.kdt_proj2_be.domain.ScrapMetalType;

import java.io.IOException;

public class ScrapMetalTypeDeserializer extends KeyDeserializer {
    @Override
    public ScrapMetalType deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        try {
            return ScrapMetalType.valueOf(key.toUpperCase()); // ENUM 변환 (대소문자 무관)
        } catch (IllegalArgumentException e) {
            throw new IOException("Invalid ScrapMetalType: " + key); // 예외 처리 추가
        }
    }
}