package com.kdt_proj2_be.config;

import com.kdt_proj2_be.handler.MyWebSocketHandler;
import com.kdt_proj2_be.persistence.MissingRecordRepository;
import com.kdt_proj2_be.persistence.TransactionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final TransactionRepository transactionRepository;
    private final MissingRecordRepository missingRecordRepository;

    // WebSocketConfig 생성자에서 두 개의 repository 주입받기
    public WebSocketConfig(TransactionRepository transactionRepository, MissingRecordRepository missingRecordRepository) {
        this.transactionRepository = transactionRepository;
        this.missingRecordRepository = missingRecordRepository;
    }

    @Bean // Bean으로 등록하여 Spring에서 관리하도록 함
    public MyWebSocketHandler myWebSocketHandler() {
        return new MyWebSocketHandler(transactionRepository, missingRecordRepository); // 두 repository를 주입
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myWebSocketHandler(), "/ws") // @Bean으로 등록된 핸들러 사용
                .setAllowedOrigins("*"); // CORS 설정
    }

    // WebSocket 메시지 크기 제한 증가 5MB
    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(5120000);
        container.setMaxBinaryMessageBufferSize(5120000);
        return container;
    }
}
