package com.server.gummymurderer.service;

import com.server.gummymurderer.domain.dto.chat.*;
import com.server.gummymurderer.domain.entity.Chat;
import com.server.gummymurderer.domain.entity.GameScenario;
import com.server.gummymurderer.domain.entity.GameSet;
import com.server.gummymurderer.domain.enum_class.ChatRoleType;
import com.server.gummymurderer.exception.AppException;
import com.server.gummymurderer.exception.ErrorCode;
import com.server.gummymurderer.repository.ChatRepository;
import com.server.gummymurderer.repository.GameScenarioRepository;
import com.server.gummymurderer.repository.GameSetRepository;
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
    private final GameSetRepository gameSetRepository;
    private final GameScenarioRepository gameScenarioRepository;

    // 채팅 보내기
    public Mono<ChatSaveResponse> saveChat(ChatSaveRequest request) {

        Optional<GameSet> optionalGameSet = gameSetRepository.findByGameSetNo(request.getGameSetNo());

        if (optionalGameSet.isEmpty()) {
            throw new AppException(ErrorCode.GAME_NOT_FOUND);
        }

        GameSet gameSet = optionalGameSet.get();

        Chat chat = ChatSaveRequest.toEntity(request, LocalDateTime.now(), ChatRoleType.USER, ChatRoleType.AI, gameSet);

        chatRepository.save(chat);

        log.info("🐻unity에서 전송한 채팅 내용: {}", chat.getChatContent());
        log.info("🐻unity에서 전송한 채팅 수신자 : {}", chat.getReceiver());
        log.info("🐻unity에서 전송한 채팅 발신자 : {}", chat.getSender());

        // AI로 메시지 전송, 수신자, 발신자, 채팅 내용 리턴
        return sendChatToAIServer(request);
    }

    // AI로 채팅 내용 전송하고 AI에서 온 답장을 반환
    private Mono<ChatSaveResponse> sendChatToAIServer(ChatSaveRequest request) {
        String aiServerUrl = "http://221.163.19.218:9090/api/chatbot/conversation_with_user";
        WebClient webClient = WebClient.builder().baseUrl(aiServerUrl).build(); // WebClient 인스턴스 생성

        // 이전 대화 내용들 가져오기
        List<Chat> previousChatContents = chatRepository.findAllByUserAndAINpcAndGameSetNo(request.getSender(), request.getReceiver(), request.getGameSetNo());

        // 이전 스토리 내용 가져오기
        Optional<GameScenario> gameScenarioOptional = gameScenarioRepository.findByGameSet_GameSetNo(request.getGameSetNo());
        String previousStory = gameScenarioOptional.map(GameScenario::getDailySummary).orElse(null);

        // AI 서버에 보낼 요청 객체 생성
        AIChatRequest aiChatRequest = new AIChatRequest();
        aiChatRequest.setSender(request.getSender());
        aiChatRequest.setReceiver(request.getReceiver());
        aiChatRequest.setChatContent(request.getChatContent());
        aiChatRequest.setChatDay(request.getChatDay());
        aiChatRequest.setPreviousStory(previousStory);

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
                .handle((aiResponse, sink) -> {
                    // AI에서 보낸 채팅 저장
                    ChatSaveRequest aiChat = new ChatSaveRequest();
                    aiChat.setSender(request.getReceiver());
                    aiChat.setReceiver(request.getSender());
                    aiChat.setChatContent(aiResponse.getChatContent());
                    aiChat.setChatDay(request.getChatDay());

                    Optional<GameSet> optionalGameSet = gameSetRepository.findByGameSetNo(request.getGameSetNo());

                    if (optionalGameSet.isEmpty()) {
                        sink.error(new AppException(ErrorCode.GAME_NOT_FOUND));
                        return;
                    }

                    GameSet gameSet = optionalGameSet.get();

                    Chat aiChatEntity = ChatSaveRequest.toEntity(aiChat, LocalDateTime.now(), ChatRoleType.AI, ChatRoleType.USER, gameSet);
                    chatRepository.save(aiChatEntity);

                    log.info("🐻AI가 전송한 채팅 내용: {}", aiChatEntity.getChatContent());
                    log.info("🐻AI가 전송한 채팅 수신자: {}", aiChatEntity.getReceiver());

                    ChatSaveResponse response = new ChatSaveResponse();
                    response.setChatContent(aiResponse.getChatContent());
                    response.setSender(aiResponse.getSender());
                    sink.next(response);
                });
    }

    // npc 채팅 요청 및 반환
    public Mono<List<NpcChatResponse>> getNpcChat(NpcChatRequestDto npcChatRequestDto) {
        return sendNpcChatToAIServer(npcChatRequestDto.getSender(), npcChatRequestDto.getNpcName1(), npcChatRequestDto.getNpcName2(), npcChatRequestDto.getGameSetNo())
                .doOnNext(npcChatAIResponse -> {
                    npcChatAIResponse.getChatContent().forEach(response -> {
                        Chat chat = NpcChatResponse.toEntity(response, npcChatRequestDto.getChatDay(), LocalDateTime.now(), ChatRoleType.AI, ChatRoleType.AI);
                        Mono.fromCallable(() -> chatRepository.save(chat)).subscribe();
                    });
                    gameSetRepository.findByGameSetNo(npcChatRequestDto.getGameSetNo())
                            .ifPresent(gameSet -> gameSet.updateGameToken(npcChatAIResponse.getTotalTokens()));
                })
                .map(NpcChatAIResponse::getChatContent);  // NpcChatAIResponse 객체의 chatContent 필드를 반환
    }

    private Mono<NpcChatAIResponse> sendNpcChatToAIServer(String sender, String npcName1, String npcName2, Long gameSetNo) {
        String aiServerUrl = "http://221.163.19.218:9090/api/chatbot/conversation_between_npcs";
        WebClient webClient = WebClient.builder().baseUrl(aiServerUrl).build();

        NpcChatRequest npcChatRequest = new NpcChatRequest();
        npcChatRequest.setSender(sender);
        npcChatRequest.setNpcName1(npcName1);
        npcChatRequest.setNpcName2(npcName2);

        gameScenarioRepository.findByGameSet_GameSetNo(gameSetNo)
                .ifPresent(gameScenario -> npcChatRequest.setPreviousStory(gameScenario.getDailySummary()));

        return webClient.post()
                .uri(aiServerUrl)
                .bodyValue(npcChatRequest)
                .retrieve()
                .bodyToMono(NpcChatAIResponse.class)
                .onErrorResume(e -> {
                    log.error("🐻AI 통신 실패 : ", e);
                    throw new AppException(ErrorCode.AI_INTERNAL_SERVER_ERROR);
                });
    }

    public List<ChatListResponse> getAllChatByUserNameAndAINpc(ChatListRequest chatListRequest) {

        List<Chat> chats = chatRepository.findAllByUserAndAINpcAndGameSetNo(chatListRequest.getUserName(), chatListRequest.getAiNpcName(), chatListRequest.getGameSetNo());

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