package com.server.gummymurderer.service;

import com.server.gummymurderer.domain.dto.chat.*;
import com.server.gummymurderer.domain.entity.*;
import com.server.gummymurderer.domain.enum_class.ChatRoleType;
import com.server.gummymurderer.exception.AppException;
import com.server.gummymurderer.exception.ErrorCode;
import com.server.gummymurderer.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final GameSetRepository gameSetRepository;
    private final GameNpcRepository gameNpcRepository;
    private final GameScenarioRepository gameScenarioRepository;
    private final GameAlibiRepository gameAlibiRepository;

    // unity 테스트용
    @Transactional
    public Mono<ChatSaveResponse> saveChatTest(Member loginMember, ChatSaveRequest request) {

        log.info("🐻chat test 시작");

        Optional<GameSet> optionalGameSet = gameSetRepository.findByGameSetNo(request.getGameSetNo());

        if (optionalGameSet.isEmpty()) {
            throw new AppException(ErrorCode.GAME_NOT_FOUND);
        }

        GameSet gameSet = optionalGameSet.get();

        request.setSender(loginMember.getAccount());

        Chat chat = ChatSaveRequest.toEntity(request, LocalDateTime.now(), ChatRoleType.USER, ChatRoleType.AI, gameSet);

        chatRepository.save(chat);

        log.info("🐻unity에서 전송한 채팅 내용: {}", chat.getChatContent());
        log.info("🐻unity에서 전송한 채팅 수신자 : {}", chat.getReceiver());
        log.info("🐻unity에서 전송한 채팅 발신자 : {}", chat.getSender());

        // ai의 답장 직접 생성
        ChatSaveResponse aiResponse = new ChatSaveResponse();
        aiResponse.setChatContent("NPC의 답장입니다.");
        aiResponse.setSender(request.getReceiver());

        log.info("🐻chat test 끝");

        return Mono.just(aiResponse);
    }

    // 채팅 보내기
    public Mono<ChatSaveResponse> saveChat(CustomUserDetails userDetails, ChatSaveRequest request) {

        Optional<GameSet> optionalGameSet = gameSetRepository.findByGameSetNo(request.getGameSetNo());

        if (optionalGameSet.isEmpty()) {
            throw new AppException(ErrorCode.GAME_NOT_FOUND);
        }

        GameSet gameSet = optionalGameSet.get();

        Member member = userDetails.getMember();
        request.setSender(member.getNickname());

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
        String aiServerUrl = "http://221.163.19.218:9090/api/user/conversation_with_user";
        WebClient webClient = WebClient.builder().baseUrl(aiServerUrl).build(); // WebClient 인스턴스 생성

        // 이전 대화 내용들 가져오기
        List<Chat> previousChatContents = chatRepository.findAllByMemberAndAINpcAndGameSetNo(request.getSender(), request.getReceiver(), request.getGameSetNo());

        // 이전 스토리 내용 가져오기
        Optional<GameScenario> gameScenarioOptional = gameScenarioRepository.findByGameSet_GameSetNo(request.getGameSetNo());
        String previousStory = gameScenarioOptional.map(GameScenario::getDailySummary).orElse("");

        // AI 서버에 보낼 요청 객체 생성
        AIChatRequest aiChatRequest = new AIChatRequest();
        aiChatRequest.setSender(request.getSender());

        // alibi 정보를 가진 Receiver 생성
        GameNpc gameNpc = gameNpcRepository.findByNpcNameAndGameSet_GameSetNo(request.getReceiver(), request.getGameSetNo())
                .orElseThrow(() -> new AppException(ErrorCode.NPC_NOT_FOUND));

        GameScenario gameScenario = gameScenarioRepository.findByGameSet_GameSetNo(request.getGameSetNo())
                        .orElseThrow(() -> new AppException(ErrorCode.SCENARIO_NOT_FOUND));

        GameAlibi gameAlibi = gameAlibiRepository.findByGameScenarioAndGameNpc(gameScenario, gameNpc)
                        .orElseThrow(() -> new AppException(ErrorCode.ALIBI_NOT_FOUND));

        Map<String, String> receiver = new HashMap<>();
        receiver.put("name", gameNpc.getNpcName());
        receiver.put("alibi", gameAlibi.getAlibi());

        aiChatRequest.setReceiver(receiver);
        aiChatRequest.setChatContent(request.getChatContent());
        aiChatRequest.setChatDay(request.getChatDay());
        aiChatRequest.setPreviousStory(previousStory);
        aiChatRequest.setSecretKey(request.getSecretKey());
        aiChatRequest.setGameNo(request.getGameSetNo());

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
                    aiChat.setChatContent(aiResponse.getAnswer().getChatContent());
                    aiChat.setChatDay(request.getChatDay());

                    // tokens 업데이트
                    gameNpc.updateTokens(aiResponse.getTokens().getPromptTokens(), aiResponse.getTokens().getCompletionTokens());

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
                    response.setChatContent(aiResponse.getAnswer().getChatContent());
                    sink.next(response);
                });
    }

    // npc 채팅 요청 및 반환
    public Mono<NpcChatResponse> getNpcChat(CustomUserDetails userDetails, NpcChatRequestDto npcChatRequestDto) {

        Optional<GameSet> optionalGameSet = gameSetRepository.findByGameSetNo(npcChatRequestDto.getGameSetNo());

        if (optionalGameSet.isEmpty()) {
            throw new AppException(ErrorCode.GAME_NOT_FOUND);
        }

        GameSet gameSet = optionalGameSet.get();

        Member member = userDetails.getMember();
        npcChatRequestDto.setSender(member.getNickname());

        return sendNpcChatToAIServer(npcChatRequestDto)
                .flatMap(npcChatResponse -> {
                    Chat chat = NpcChatResponse.toEntity(npcChatResponse, npcChatRequestDto.getChatDay(), LocalDateTime.now(), ChatRoleType.AI, ChatRoleType.AI, gameSet);  // 변경된 부분
                    Mono<Chat> savedChat = Mono.fromCallable(() -> chatRepository.save(chat));

                    // tokens 업데이트
                    GameNpc gameNpc = gameNpcRepository.findByNpcNameAndGameSet_GameSetNo(npcChatResponse.getSender(), npcChatRequestDto.getGameSetNo())
                            .orElseThrow(() -> new AppException(ErrorCode.NPC_NOT_FOUND));

                    gameNpc.updateTokens(npcChatResponse.getTokens().getPromptTokens(), npcChatResponse.getTokens().getCompletionTokens());

                    return savedChat.map(c -> npcChatResponse);  // 저장된 채팅을 반환
                });
    }

    private Mono<NpcChatResponse> sendNpcChatToAIServer(NpcChatRequestDto npcChatRequestDto) {
        String aiServerUrl = "http://221.163.19.218:9090/api/chatbot/conversation_between_npcs";
        WebClient webClient = WebClient.builder().baseUrl(aiServerUrl).build();

        NpcChatRequest npcChatRequest = NpcChatRequest.builder()
                .gameSetNo(npcChatRequestDto.getGameSetNo())
                .secretKey(npcChatRequestDto.getSecretKey())
                .sender(npcChatRequestDto.getSender())
                .npcName1(npcChatRequestDto.getNpcName1())
                .npcName2(npcChatRequestDto.getNpcName2())
                .chatDay(npcChatRequestDto.getChatDay())
                .previousStory(gameScenarioRepository.findByGameSet_GameSetNo(npcChatRequestDto.getGameSetNo())
                        .map(GameScenario::getDailySummary).orElse(""))
                .build();

        return webClient.post()
                .uri(aiServerUrl)
                .bodyValue(npcChatRequest)
                .retrieve()
                .bodyToMono(NpcChatResponse.class)
                .onErrorResume(e -> {
                    log.error("🐻AI 통신 실패 : ", e);
                    throw new AppException(ErrorCode.AI_INTERNAL_SERVER_ERROR);
                });
    }

    public List<ChatListResponse> getAllChatByUserNameAndAINpc(Member loginMember, ChatListRequest chatListRequest) {

        Optional<GameSet> optionalGameSet = gameSetRepository.findByGameSetNo(chatListRequest.getGameSetNo());

        if (optionalGameSet.isEmpty()) {
            throw new AppException(ErrorCode.GAME_NOT_FOUND);
        }

        chatListRequest.setNickName(loginMember.getAccount());

        List<Chat> chats = chatRepository.findAllByMemberAndAINpcAndGameSetNo(chatListRequest.getNickName(), chatListRequest.getAiNpcName(), chatListRequest.getGameSetNo());

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