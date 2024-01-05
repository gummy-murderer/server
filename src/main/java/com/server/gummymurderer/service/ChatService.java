package com.server.gummymurderer.service;

import com.server.gummymurderer.configuration.WebSocketChatHandler;
import com.server.gummymurderer.domain.dto.chat.ChatSaveRequest;
import com.server.gummymurderer.domain.dto.chat.ChatSaveResponse;
import com.server.gummymurderer.domain.entity.Chat;
import com.server.gummymurderer.domain.enum_class.ChatRoleType;
import com.server.gummymurderer.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    @Autowired
    @Lazy
    private WebSocketChatHandler chatHandler;  // WebSocketChatHandler 를 사용하기 위해 추가

    // 채팅 보내기
    public ChatSaveResponse sendChat(ChatSaveRequest request) {

        request.setRoleTypesBasedOnAI();
        Chat chat = ChatSaveRequest.toEntity(request, LocalDateTime.now());

        chatRepository.save(chat);

        // 채팅 수신자의 타입에 따라 다르게 처리
        if (chat.getReceiverType() == ChatRoleType.AI) {
            // 수신자가 AI NPC인 경우 AI 서버로 메시지 전송
            chatHandler.sendMessageToAIServer(chat.getChatContent());
        } else {
            // 수신자가 사용자인 경우 Unity 서버로 메시지 전송
            chatHandler.sendMessageToUnityServer(chat.getChatContent());
        }

        return ChatSaveResponse.of(chat);
    }

}
