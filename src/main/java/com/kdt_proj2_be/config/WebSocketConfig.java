package com.kdt_proj2_be.config;

//import com.kdt_proj2_be.handler.MyWebSocketHandler;
//import com.kdt_proj2_be.persistence.TransactionRepository;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.socket.config.annotation.EnableWebSocket;
//import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
//import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
//
//@Configuration
//@EnableWebSocket
//public class WebSocketConfig implements WebSocketConfigurer {
//
//    private final TransactionRepository transactionRepository;
//
//    public WebSocketConfig(TransactionRepository transactionRepository) {
//        this.transactionRepository = transactionRepository;
//    }
//
//    @Bean // ✅ Bean으로 등록하여 Spring에서 관리하도록 함
//    public MyWebSocketHandler myWebSocketHandler() {
//        return new MyWebSocketHandler(transactionRepository);
//    }
//
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(MyWebSocketHandler(), "/ws")
//                .setAllowedOrigins("*"); // CORS 설정 (허용 도메인)
//    }
//}

import com.kdt_proj2_be.handler.MyWebSocketHandler;
import com.kdt_proj2_be.persistence.TransactionRepository;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final TransactionRepository transactionRepository;

    public WebSocketConfig(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Bean // Bean으로 등록하여 Spring에서 관리하도록 함
    public MyWebSocketHandler myWebSocketHandler() {
        return new MyWebSocketHandler(transactionRepository); // TransactionRepository 주입
    }

//    @Bean
//    public ServletWebServerFactory webServerFactory() {
//        JettyServletWebServerFactory factory = new JettyServletWebServerFactory(8081);
//        return factory;
//    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myWebSocketHandler(), "/ws") // @Bean으로 등록된 핸들러 사용
                .setAllowedOrigins("*"); // CORS 설정
    }
}
