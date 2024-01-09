package com.server.gummymurderer.service;

import com.server.gummymurderer.domain.dto.chat.AIChatResponse;
import com.server.gummymurderer.domain.dto.chat.ChatListResponse;
import com.server.gummymurderer.domain.dto.chat.ChatSaveRequest;
import com.server.gummymurderer.domain.dto.chat.ChatSaveResponse;
import com.server.gummymurderer.domain.entity.Chat;
import com.server.gummymurderer.domain.enum_class.ChatRoleType;
import com.server.gummymurderer.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    // 채팅 보내기
    public Mono<ChatSaveResponse> saveChat(ChatSaveRequest request) {

        Chat chat = ChatSaveRequest.toEntity(request, LocalDateTime.now(), ChatRoleType.USER, ChatRoleType.AI);

        chatRepository.save(chat);

        log.info("🐻unity에서 전송한 채팅 내용: {}", chat.getChatContent());
        log.info("🐻unity에서 전송한 채팅 수신자 : {}", chat.getReceiver());
        log.info("🐻unity에서 전송한 채팅 발신자 : {}", chat.getSender());

        // AI로 메시지 전송, 수신자, 발신자, 채팅 내용 리턴
        return sendChatToAIServer(request);
    }

    //유니티 테스트용 메소드
//    public Mono<ChatSaveResponse> saveChat(ChatSaveRequest request) {
//        System.out.println("🐻service 로직 시작");
//
//        Chat chat = ChatSaveRequest.toEntity(request, LocalDateTime.now(), ChatRoleType.USER, ChatRoleType.AI);
//
//        chatRepository.save(chat);
//
//        log.info("🐻unity에서 전송한 채팅 내용: {}", chat.getChatContent());
//        log.info("🐻unity에서 전송한 채팅 수신자 : {}", chat.getReceiver());
//        log.info("🐻unity에서 전송한 채팅 발신자 : {}", chat.getSender());
//
//        // AI로 메시지를 전송하는 부분 제거
//        // 유니티에서 보낸 채팅을 받았다는 응답을 반환
//        ChatSaveResponse response = new ChatSaveResponse();
//        response.setChatContent(chat.getChatContent());
//        response.setSender(chat.getSender());
//
//        return Mono.just(response);
//    }

    // AI로 채팅 내용 전송하고 AI에서 온 답장을 반환
    private Mono<ChatSaveResponse> sendChatToAIServer(ChatSaveRequest request) {
        String aiServerUrl = "AI server url";
        WebClient webClient = WebClient.builder().baseUrl(aiServerUrl).build(); // WebClient 인스턴스 생성

        return webClient.post()
                .uri(aiServerUrl)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AIChatResponse.class)
                .map(aiResponse -> {
                    // AI에서 보낸 채팅 저장
                    ChatSaveRequest aiChat = new ChatSaveRequest();
                    aiChat.setSender(aiResponse.getSender());
                    aiChat.setReceiver(request.getSender());
                    aiChat.setChatContent(aiResponse.getChatContent());
                    aiChat.setChatDay(request.getChatDay());
                    Chat aiChatEntity = ChatSaveRequest.toEntity(aiChat, LocalDateTime.now(), ChatRoleType.AI, ChatRoleType.USER);
                    chatRepository.save(aiChatEntity);

                    log.info("🐻AI가 전송한 채팅 내용: {}", aiChatEntity.getChatContent());
                    log.info("🐻AI가 전송한 채팅 수신자: {}", aiChatEntity.getReceiver());

                    ChatSaveResponse response = new ChatSaveResponse();
                    response.setChatContent(aiResponse.getChatContent());
                    response.setSender(aiResponse.getSender());
                    return response;
                });
    }

    public List<ChatListResponse> getAllChatByUserNameAndAINpc(String userName, String aiNpcName) {

        List<Chat> chats = chatRepository.findAllByUserAndAINpc(userName, aiNpcName);

        List<ChatListResponse> chatListResponses = new ArrayList<>();
        for (Chat chat : chats) {
            chatListResponses.add(ChatListResponse.of(chat));
        }
        return chatListResponses;
    }

}