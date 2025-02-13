package com.kdt_proj2_be.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // LocalDateTime 지원 모듈
import com.kdt_proj2_be.domain.MissingRecord;
import com.kdt_proj2_be.domain.Transaction;
import com.kdt_proj2_be.dto.EntryExitStatusDTO;
import com.kdt_proj2_be.dto.MissingRecordDTO;
import com.kdt_proj2_be.persistence.MissingRecordRepository;
import com.kdt_proj2_be.persistence.TransactionRepository;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class MyWebSocketHandler extends TextWebSocketHandler {

    private static final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final ObjectMapper objectMapper; // JSON 변환기
    private final TransactionRepository transactionRepository; // TransactionRepository 주입
    private final MissingRecordRepository missingRecordRepository;

    // 생성자에서 Repository 주입받기
    public MyWebSocketHandler(TransactionRepository transactionRepository, MissingRecordRepository missingRecordRepository) {
        this.transactionRepository = transactionRepository;
        this.missingRecordRepository = missingRecordRepository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // JSON을 문자열 포맷으로 직렬화
    }

    // 웹소켓에 연결되면 바로 EntryExitStatus를 보내도록 변경(updatedAt내림차순 정렬문제 때문에 이렇게 했는데 잘 작동할지 모르겠다.)
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("클라이언트 연결됨: " + session.getId());
        sessions.add(session);
//        session.sendMessage(new TextMessage("서버에 연결되었습니다."));

        // 클라이언트 연결되면 출입 현황 데이터를 전송
        sendEntryExitStatus(session);
    }

    // 모든 연결된 클라이언트 JSON으로 전송
    public void sendTransactions() throws Exception {
        List<Transaction> transactionList = transactionRepository.findAll(); // DB에서 모든 트랜잭션 가져오기
        String transactionsPayload = objectMapper.writeValueAsString(transactionList); // JSON 변환

        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(transactionsPayload)); // JSON 전송
            }
        }
    }

    // 요청을 보낸 클라이언트 한 곳에 JSON으로 전송
    private void sendTransactionList(WebSocketSession session) throws Exception {
        List<Transaction> transactionList = transactionRepository.findAll();
        String transactionsPayload = objectMapper.writeValueAsString(transactionList);

        session.sendMessage(new TextMessage(transactionsPayload));
    }

//    // 출입 현황 데이터를 전송하는 메서드
//    public void sendEntryExitStatus(WebSocketSession session) throws Exception {
//        List<Transaction> transactions = transactionRepository.findAll();
//
//        // 트랜잭션을 10개씩 나누어 전송 (Chunking)
//        final int CHUNK_SIZE = 10;
//        for (int i = 0; i < transactions.size(); i += CHUNK_SIZE) {
//            List<Transaction> chunk = transactions.subList(i, Math.min(i + CHUNK_SIZE, transactions.size()));
//
//            // DTO 변환 및 업데이트 시간 기준으로 내림차순 정렬
//            List<EntryExitStatusDTO> entryExitStatus = transactions.stream()
//                    .map(EntryExitStatusDTO::fromEntity)
//                    .sorted((t1, t2) -> t2.getUpdatedAt().compareTo(t1.getUpdatedAt()))  // updatedAt 기준 내림차순 정렬
//                    .collect(Collectors.toList());
//
//            String entryExitStatusPayload = objectMapper.writeValueAsString(entryExitStatus);
//            session.sendMessage(new TextMessage(entryExitStatusPayload));
//        }
//    }

    public void sendEntryExitStatus(WebSocketSession session) throws Exception {
        // 트랜잭션 데이터를 최신 순으로 정렬 (updatedAt 기준)
        List<Transaction> transactions = transactionRepository.findAllByOrderByUpdatedAtDesc();
//                .sorted((t1, t2) -> t2.getUpdatedAt().compareTo(t1.getUpdatedAt()))  // updatedAt 기준 내림차순 정렬
//                .collect(Collectors.toList());

        // DTO 변환
        List<EntryExitStatusDTO> entryExitStatus = transactions.stream()
                .map(transaction -> {
                    EntryExitStatusDTO dto = EntryExitStatusDTO.fromEntity(transaction);
                    // 추가 필드 계산
                    dto.setEntryWeight(transaction.getEntryWeight());
                    dto.setExitWeight(transaction.getExitWeight());
                    dto.setTotalWeight(transaction.getTotalWeight());
                    dto.setUpdatedAt(transaction.getUpdatedAt());
                    return dto;
                })
                .collect(Collectors.toList());

        // JSON으로 변환하여 클라이언트에 전송
        String entryExitStatusPayload = objectMapper.writeValueAsString(entryExitStatus);
        session.sendMessage(new TextMessage(entryExitStatusPayload));
    }

    public void sendMissingRecords(WebSocketSession session) throws Exception {
        // MissingRecord 테이블에서 데이터를 조회하고, checkedAt 기준 내림차순으로 정렬
        List<MissingRecord> missingRecords = missingRecordRepository.findAll().stream()
                .sorted((r1, r2) -> r2.getCheckedAt().compareTo(r1.getCheckedAt()))  // checkedAt 기준 내림차순 정렬
                .collect(Collectors.toList());

        // DTO로 변환
        List<MissingRecordDTO> missingRecordDTOList = missingRecords.stream()
                .map(missingRecord -> new MissingRecordDTO(
                        missingRecord.getCarNumber(),
                        missingRecord.getCheckedAt(),
                        missingRecord.getExitTime(),
                        missingRecord.getExitWeight(),
                        missingRecord.getOutImg1(),
                        missingRecord.getOutImg2(),
                        missingRecord.getOutImg3()
                ))
                .collect(Collectors.toList());

        // JSON으로 변환하여 클라이언트에 전송
        String missingRecordsPayload = objectMapper.writeValueAsString(missingRecordDTOList);
        session.sendMessage(new TextMessage(missingRecordsPayload));
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
                sendTransactionList(session); // Transacrion 테이블 프론트에 전송
            }
            else if ("getEntryExitStatus".equals(request.getAction())) {
                sendEntryExitStatus(session); // 출입 현황 프론트에 전송
            }
            else if ("getMissingRecords".equals(request.getAction())) {
                sendMissingRecords(session); // 출입 현황 프론트에 전송
            }
            else {
                session.sendMessage(new TextMessage("알 수 없는 요청: " + request.getAction()));
            }
        } catch (Exception e) {
            session.sendMessage(new TextMessage("JSON 파싱 오류: " + e.getMessage()));
        }
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
