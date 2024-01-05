package com.server.gummymurderer.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.gummymurderer.domain.dto.chat.ChatSaveRequest;
import com.server.gummymurderer.service.ChatService;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
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

    private Map<String, WebSocketSession> unitySessions = new ConcurrentHashMap<>();
    private final Map<WebSocketSession, Long> sessionLastActiveTimes = new ConcurrentHashMap<>();
    private final ChatService chatService;
    private static final long TIMEOUT_MILLIS = 60000;  // 타임아웃 시간을 1분으로 설정
    private Session aiClientSession;

    public void connectToAIServer() throws Exception {
        //AI 서버로의 웹소켓 연결을 수립하며, 연결 세션은 aiClientSession에 저장
        String aiServerUrl = "ai server webSocket url";
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.aiClientSession = container.connectToServer(AIClient.class, new URI(aiServerUrl));
    }

    public void sendMessageToAIServer(String message) {
        // aiClientSession을 통해 AI 서버로 메시지를 비동기적으로 전송
        this.aiClientSession.getAsyncRemote().sendText(message);
    }

    public void sendMessageToUnityServer(String message) {
        //unitySessions에 저장된 모든 Unity 서버 세션에 메시지를 전송
        for (WebSocketSession session : unitySessions.values()) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessionLastActiveTimes.put(session, System.currentTimeMillis());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info(session.getId() + "로부터 메시지 수신: " + message.getPayload());

        ObjectMapper mapper = new ObjectMapper();
        ChatSaveRequest chatSaveRequest = mapper.readValue(message.getPayload(), ChatSaveRequest.class);

        chatService.sendChat(chatSaveRequest);  // 채팅 저장

        session.sendMessage(message);
        log.info(session.getId() + "에 메시지 발송: " + message.getPayload());

        sessionLastActiveTimes.put(session, System.currentTimeMillis());

        // Unity 서버에 메시지를 보낸다.
        sendMessageToUnityServer(message.getPayload());
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionLastActiveTimes.remove(session);
    }

    @Scheduled(fixedRate = 60000)  // 1분마다 실행
    public void checkAndCloseInactiveSessions() {
        //설정된 타임아웃 시간 동안 활성화되지 않은 웹소켓 세션을 종료
        long now = System.currentTimeMillis();
        sessionLastActiveTimes.entrySet().removeIf(entry -> {
            try {
                if (now - entry.getValue() > TIMEOUT_MILLIS) {
                    entry.getKey().close();
                    return true;
                }
                return false;
            } catch (IOException e) {
                return true;
            }
        });
    }
}
