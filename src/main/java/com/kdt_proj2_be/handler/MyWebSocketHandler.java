package com.kdt_proj2_be.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // LocalDateTi8me 지원 모듈
import com.kdt_proj2_be.domain.Transaction;
import com.kdt_proj2_be.persistence.TransactionRepository;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MyWebSocketHandler extends TextWebSocketHandler {

    private static final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final ObjectMapper objectMapper; // JSON 변환기
    private final TransactionRepository transactionRepository; // ✅ TransactionRepository 주입

    // ✅ 생성자에서 TransactionRepository 주입받기
    public MyWebSocketHandler(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // ✅ JSON을 문자열 포맷으로 직렬화

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("클라이언트 연결됨: " + session.getId());
        sessions.add(session);
//        session.sendMessage(new TextMessage("서버에 연결되었습니다."));
    }

    // WebSocket을 통해 List<Transaction> JSON으로 전송
    public void sendTransactionList() throws Exception {
        List<Transaction> transactionList = transactionRepository.findAll(); // DB에서 모든 트랜잭션 가져오기
        String jsonList = objectMapper.writeValueAsString(transactionList); // JSON 변환

        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(jsonList)); // JSON List 전송
            }
        }
    }

    // 클라이언트의 요청을 처리하는 메서드
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("수신 메시지: " + payload);

        try {
            // JSON을 파싱하여 요청 내용 확인
            WebSocketRequest request = objectMapper.readValue(payload, WebSocketRequest.class);

            if ("getTransactions".equals(request.getAction())) {
                sendTransactionList(session); // 트랜잭션 리스트 전송
            } else {
                session.sendMessage(new TextMessage("알 수 없는 요청: " + request.getAction()));
            }
        } catch (Exception e) {
            session.sendMessage(new TextMessage("JSON 파싱 오류: " + e.getMessage()));
        }
    }

    // 클라이언트에게 트랜잭션 리스트 전송
    private void sendTransactionList(WebSocketSession session) throws Exception {
        List<Transaction> transactionList = transactionRepository.findAll();
        String jsonList = objectMapper.writeValueAsString(transactionList);

        session.sendMessage(new TextMessage(jsonList));
    }

    // 클라이언트와 연결 종료
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        System.out.println("클라이언트 연결 종료: " + session.getId());
        sessions.remove(session);
    }

    // 클라이언트 요청을 받을 DTO 클래스
    private static class WebSocketRequest {
        private String action;

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }
    }

}
