package com.server.gummymurderer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.gummymurderer.domain.dto.alibi.AlibiDTO;
import com.server.gummymurderer.domain.dto.gameNpc.GameNpcDTO;
import com.server.gummymurderer.domain.dto.scenario.*;
import com.server.gummymurderer.domain.entity.*;
import com.server.gummymurderer.domain.enum_class.GameResult;
import com.server.gummymurderer.exception.AppException;
import com.server.gummymurderer.exception.ErrorCode;
import com.server.gummymurderer.repository.GameAlibiRepository;
import com.server.gummymurderer.repository.GameNpcRepository;
import com.server.gummymurderer.repository.GameScenarioRepository;
import com.server.gummymurderer.repository.GameSetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ScenarioService {

    private final GameSetRepository gameSetRepository;
    private final GameNpcRepository gameNpcRepository;
    private final GameScenarioRepository gameScenarioRepository;
    private final GameAlibiRepository gameAlibiRepository;

    @Value("${ai.url}")
    private String aiUrl;

    @Transactional
    public MakeScenarioResponse makeScenario(MakeScenarioRequest request, Member loginMember) throws JsonProcessingException {

        System.out.println("🐻scenario 요청 시작");

        // 일치하는 게임이 없을경우 에러 발생
        GameSet foundGameSet = gameSetRepository.findByGameSetNoAndMember(request.getGameSetNo(), loginMember)
                .orElseThrow(() -> new AppException(ErrorCode.GAME_SET_NOT_FOUND));

        // AI에게 시나리오 생성 요청보내는 로직
        List<NpcInfo> aliveGameNpcList = gameNpcRepository.findAllAliveResidentNpcInfoByGameSetNo(foundGameSet.getGameSetNo());
        String murderName = gameNpcRepository.findMurderByGameSetNo(foundGameSet.getGameSetNo());
        log.info("🤖 머더러 이름 : {}", murderName);
        log.info("🤖 secret key : {}", request.getSecretKey());
        int day = foundGameSet.getGameDay();
        log.info("🤖 day : {} 일차", day);
        String previousStory = foundGameSet.getGameSummary();
        log.info("🤖 previousStory : {} ", previousStory);

        String url =  aiUrl + "/api/scenario/generate_victim";

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("gameNo", foundGameSet.getGameSetNo());
        requestData.put("secretKey", request.getSecretKey());
        requestData.put("day", day);
        requestData.put("murderer", murderName);
        requestData.put("livingCharacters", aliveGameNpcList);
        requestData.put("previousStory", previousStory);

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonRequest = objectMapper.writeValueAsString(requestData);
        log.info("🤖 jsonRequest : {}", jsonRequest);

        WebClient webClient = WebClient.create();

        AiMakeScenarioResponse result = webClient
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(jsonRequest))
                .retrieve()
                .bodyToMono(AiMakeScenarioResponse.class)
                .block();

        log.info("🐻 result victim : {}", result.getAnswer().getVictim());
        log.info("🐻 result token : {}", result.getTokens().getTotalTokens());
        log.info("🐻 result dailySummary: {}", result.getAnswer().getDailySummary());
        log.info("🐻 result alibis: {}", result.getAnswer().getAlibis());

        GameScenario savedGameScenario = gameScenarioRepository.save(new GameScenario(result, foundGameSet));

        // 피해자 NpcStatus Dead로 변경
        String victim = result.getAnswer().getVictim();
        GameNpc victimNpc = gameNpcRepository.findByNpcNameAndGameSet(victim, foundGameSet)
                .orElseThrow(() -> new AppException(ErrorCode.NPC_NOT_FOUND));
        log.info("🐻 피해자 npc : {}", victimNpc);

        victimNpc.markDeath(savedGameScenario.getCrimeScene());
        gameNpcRepository.save(victimNpc);

        // Alibi 정보를 GameAlibi에 저장
        for (AlibiDTO alibiDTO : result.getAnswer().getAlibis()) {

            GameNpc gameNpc = gameNpcRepository.findByGameNpcNo(alibiDTO.getGameNpcNo())
                    .orElseThrow(() -> new AppException(ErrorCode.NPC_NOT_FOUND));

            // AlibiDTO 정보 확인
            log.info("🐻 AlibiDTO Information: {}", alibiDTO);

            GameAlibi gameAlibi = alibiDTO.toEntity(savedGameScenario, gameNpc);
            gameAlibiRepository.save(gameAlibi);
        }

        // 해당 게임의 npc list
        List<GameNpc> gameNpcs = gameNpcRepository.findAllByGameSet(foundGameSet);

        List<GameNpcDTO> npcList = new ArrayList<>();
        for (GameNpc gameNpc : gameNpcs) {
            GameNpcDTO dto = new GameNpcDTO(gameNpc);
            npcList.add(dto);
        }

        MakeScenarioResponse response = MakeScenarioResponse.of(savedGameScenario, npcList);

        log.info("🐻scenario 완료");

        return response;
    }

    public IntroAnswerDTO intro(IntroRequest request, Member loginMember) throws JsonProcessingException{

        log.info("🐻intro 요청 시작");

        // 일치하는 게임이 없을경우 에러 발생
        GameSet foundGameSet = gameSetRepository.findByGameSetNoAndMember(request.getGameSetNo(), loginMember)
                .orElseThrow(() -> new AppException(ErrorCode.GAME_SET_NOT_FOUND));

        String url = aiUrl + "/api/scenario/generate_intro";

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("gameNo", foundGameSet.getGameSetNo());
        requestData.put("secretKey", request.getSecretKey());
        requestData.put("characters", request.getCharacters());

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonRequest = objectMapper.writeValueAsString(requestData);
        log.info("🐻jsonRequest : {} ", jsonRequest);

        WebClient webClient = WebClient.create();

        IntroResponse result = webClient
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(jsonRequest))
                .retrieve()
                .bodyToMono(IntroResponse.class)
                .block();

        log.info("🐻 result Greeting : {}", result.getAnswer().getGreeting());
        log.info("🐻 result Content : {}", result.getAnswer().getContent());
        log.info("🐻 result Closing : {}", result.getAnswer().getClosing());

        log.info("🐻intro 완료");

        return result.getAnswer();
    }

    public FinalWordAnswerDTO finalWords(FinalWordRequest request, Member loginMember) throws JsonProcessingException {

        log.info("🐻finalWords 요청 시작");

        // 일치하는 게임이 없을 경우 에러 발생
        GameSet foundGameSet = gameSetRepository.findByGameSetNoAndMember(request.getGameSetNo(), loginMember)
                .orElseThrow(() -> new AppException(ErrorCode.GAME_SET_NOT_FOUND));

        // gameResult 정보 가져오기
        String gameResult = null;

        if (foundGameSet.getGameResult() == GameResult.SUCCESS) {
            gameResult = "victory";
        } else {
            throw new AppException(ErrorCode.GAME_NOT_WON);
        }

        log.info("🐻 gameResult : {}", foundGameSet.getGameResult());

        // murderer 정보 가져오기
        String murderer = gameNpcRepository.findMurderByGameSetNo(foundGameSet.getGameSetNo());

        // previousStory 정보 가져오기
        String previousStory = gameScenarioRepository.findTopByGameSetOrderByScenarioNoDesc(foundGameSet)
                .map(GameScenario::getDailySummary)
                .orElseThrow(() -> new AppException(ErrorCode.SCENARIO_NOT_FOUND));

        // livingCharacters 정보 가져오기
        List<NpcInfo> livingCharacters = gameNpcRepository.findAllAliveResidentNpcInfoByGameSetNo(foundGameSet.getGameSetNo());

        String url = aiUrl + "/api/scenario/generate_final_words";

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("gameNo", foundGameSet.getGameSetNo());
        requestData.put("secretKey", request.getSecretKey());
        requestData.put("gameResult", gameResult);
        requestData.put("murderer", murderer);
        requestData.put("livingCharacters", livingCharacters);
        requestData.put("previousStory", previousStory);

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonRequest = objectMapper.writeValueAsString(requestData);
        log.info("🐻jsonRequest : {} ", jsonRequest);

        WebClient webClient = WebClient.create();

        FinalWordResponse result = webClient
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(jsonRequest))
                .retrieve()
                .bodyToMono(FinalWordResponse.class)
                .block();

        log.info("🐻 result finalWords : {}", result.getAnswer().getFinalWords());
        log.info("🐻finalWords 완료");

        return result.getAnswer();
    }

    @Transactional
    public IntroAndScenarioResponse makeIntroAndScenario (IntroRequest introRequest, MakeScenarioRequest makeScenarioRequest,  Member loginMember) throws JsonProcessingException {

        IntroAnswerDTO introAnswerDTO = intro(introRequest, loginMember);
        MakeScenarioResponse makeScenarioResponse = makeScenario(makeScenarioRequest, loginMember);

        return new IntroAndScenarioResponse(introAnswerDTO, makeScenarioResponse);

    }

}
