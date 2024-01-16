package com.server.gummymurderer.service;

import com.server.gummymurderer.domain.dto.chat.*;
import com.server.gummymurderer.domain.entity.Chat;
import com.server.gummymurderer.domain.entity.Npc;
import com.server.gummymurderer.domain.enum_class.ChatRoleType;
import com.server.gummymurderer.exception.AppException;
import com.server.gummymurderer.exception.ErrorCode;
import com.server.gummymurderer.repository.ChatRepository;
import com.server.gummymurderer.repository.NpcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final NpcRepository npcRepository;

    // 채팅 보내기
    public Mono<ChatSaveResponse> saveChat(ChatSaveRequest request) {

        Optional<Npc> npc = npcRepository.findByNpcName(request.getReceiver());
        if (npc.isEmpty()) {
            return Mono.error(new AppException(ErrorCode.NPC_NOT_FOUND));
        }

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
        String aiServerUrl = "http://221.163.19.218:9090/api/chatbot/conversation_with_user";
        WebClient webClient = WebClient.builder().baseUrl(aiServerUrl).build(); // WebClient 인스턴스 생성

        // 이전 대화 내용들 가져오기
        List<Chat> previousChatContents = chatRepository.findAllByUserAndAINpc(request.getSender(), request.getReceiver());

        Npc npc = npcRepository.findByNpcName(request.getReceiver())
                .orElseThrow(() -> new AppException(ErrorCode.NPC_NOT_FOUND));

        // AI 서버에 보낼 요청 객체 생성
        AIChatRequest aiChatRequest = new AIChatRequest();
        aiChatRequest.setSender(request.getSender());
        aiChatRequest.setReceiver(request.getReceiver());
        aiChatRequest.setChatContent(request.getChatContent());
        aiChatRequest.setChatDay(request.getChatDay());

        //Npc 정보 추가
        aiChatRequest.setNpcName(npc.getNpcName());
        aiChatRequest.setNpcPersonality(npc.getNpcPersonality());
        aiChatRequest.setNpcPersonalityDescription(npc.getNpcPersonalityDescription());
        aiChatRequest.setNpcFeature(npc.getNpcFeature());
        aiChatRequest.setNpcFeatureDescription(npc.getNpcFeatureDescription());

        // 이전 채팅 내용에서 필요한 정보만 추출
        List<Map<String, Object>> simplifiedPreviousChats = previousChatContents.stream()
                .map(chat -> {
                    Map<String, Object> simpleChat = new HashMap<>();
                    simpleChat.put("sender", chat.getSender());
                    simpleChat.put("receiver", chat.getReceiver());
                    simpleChat.put("chatContent", chat.getChatContent());
                    simpleChat.put("chatDay", chat.getChatDay());
                    return simpleChat;
                })
                .collect(Collectors.toList());

        // 현재 채팅 내용을 리스트에서 제거
        simplifiedPreviousChats = simplifiedPreviousChats.stream()
                        .filter(chat ->
                                !(chat.get("sender").equals(aiChatRequest.getSender()) &&
                                chat.get("receiver").equals(aiChatRequest.getReceiver())&&
                                chat.get("chatContent").equals(aiChatRequest.getChatContent())))
                .collect(Collectors.toList());

        aiChatRequest.setPreviousChatContents(simplifiedPreviousChats);

        return webClient.post()
                .uri(aiServerUrl)
                .bodyValue(aiChatRequest)
                .retrieve()
                .bodyToMono(AIChatResponse.class)
                .onErrorResume(e -> {
                    log.error("🐻AI 통신 실패 : ", e);
                    throw new AppException(ErrorCode.AI_INTERNAL_SERVER_ERROR);
                })
                .map(aiResponse -> {
                    // AI에서 보낸 채팅 저장
                    ChatSaveRequest aiChat = new ChatSaveRequest();
                    aiChat.setSender(request.getReceiver());
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

    // npc 채팅 요청 및 반환
    public Mono<List<NpcChatResponse>> getNpcChat(String npcName1, String npcName2) {
        return sendNpcChatToAIServer(npcName1, npcName2)
                .doOnNext(responseList -> {
                    responseList.forEach(response -> {
                        Chat chat = NpcChatResponse.toEntity(response, LocalDateTime.now(), ChatRoleType.AI, ChatRoleType.AI);
                        // Mono.fromCallable을 사용하여 이 작업을 비동기 방식으로 수행
                        // subscribe를 호출하여 실제로 이 작업을 수행하도록 한다.
                        Mono.fromCallable(() -> chatRepository.save(chat)).subscribe();
                    });
                });
    }

    private Mono<List<NpcChatResponse>> sendNpcChatToAIServer(String npcName1, String npcName2) {
        String aiServerUrl = "";
        WebClient webClient = WebClient.builder().baseUrl(aiServerUrl).build();

        NpcChatRequest npcChatRequest = new NpcChatRequest();
        npcChatRequest.setNpcName1(npcName1);
        npcChatRequest.setNpcName2(npcName2);

        // 요청 본문에는 npcChatRequest를 설정하고, 응답 본문은 NpcChatResponse 클래스로 변환
        return webClient.post()
                .uri(aiServerUrl)
                .bodyValue(npcChatRequest)
                .retrieve() //요청을 전송하고 응답을 받아오는 역할
                .bodyToFlux(NpcChatResponse.class)
                .onErrorResume(e -> {
                    log.error("🐻AI 통신 실패 : ", e);
                    throw new AppException(ErrorCode.AI_INTERNAL_SERVER_ERROR);
                })

                // collectList 연산자를 사용하여 NpcChatResponse 스트림의 모든 항목을 리스트로 모음
                // Flux<NpcChatResponse>를 Mono<List<NpcChatResponse>>로 변환
                .collectList();
    }


    public List<ChatListResponse> getAllChatByUserNameAndAINpc(String userName, String aiNpcName) {

        List<Chat> chats = chatRepository.findAllByUserAndAINpc(userName, aiNpcName);

        if (chats.isEmpty()) {
            throw new AppException(ErrorCode.NO_CHAT_HISTORY);
        }

        List<ChatListResponse> chatListResponses = new ArrayList<>();
        for (Chat chat : chats) {
            chatListResponses.add(ChatListResponse.of(chat));
        }
        return chatListResponses;
    }

}