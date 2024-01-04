package com.server.gummymurderer.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.gummymurderer.domain.dto.chat.ChatSaveRequest;
import com.server.gummymurderer.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 * WebSocket Handler 작성
 * 소켓 통신은 서버와 클라이언트가 1:n으로 관계를 맺는다. 따라서 한 서버에 여러 클라이언트 접속 가능
 * 서버에는 여러 클라이언트가 발송한 메세지를 받아 처리해줄 핸들러가 필요
 * TextWebSocketHandler를 상속받아 핸들러 작성
 * 클라이언트로 받은 메세지를 log로 출력하고 클라이언트로 환영 메세지를 보내줌
 * */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {

    private Map<String, Map<String, WebSocketSession>> chatRooms = new ConcurrentHashMap<>();
    private final ChatService chatService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info(session.getId() + " 연결됨");
        String chatRoomNo = getChatRoomNo(session);
        chatRooms.computeIfAbsent(chatRoomNo, k -> new ConcurrentHashMap<>()).put(session.getId(), session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info(session.getId() + "로부터 메시지 수신: " + message.getPayload());
        String chatRoomNo = getChatRoomNo(session);

        ObjectMapper mapper = new ObjectMapper();
        ChatSaveRequest chatSaveRequest = mapper.readValue(message.getPayload(), ChatSaveRequest.class);

        chatService.sendChat(chatSaveRequest);  // 채팅 저장

        for (WebSocketSession s : chatRooms.get(chatRoomNo).values()) {
            s.sendMessage(message);
            log.info(s.getId() + "에 메시지 발송: " + message.getPayload());
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info(session.getId() + " 연결 종료");
        String chatRoomNo = getChatRoomNo(session);
        chatRooms.get(chatRoomNo).remove(session.getId());
    }

    private String getChatRoomNo(WebSocketSession session) {
        String path = session.getUri().getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }
}
