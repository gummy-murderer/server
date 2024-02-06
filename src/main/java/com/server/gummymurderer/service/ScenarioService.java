package com.server.gummymurderer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.gummymurderer.domain.dto.alibi.AlibiDTO;
import com.server.gummymurderer.domain.dto.scenario.AiMakeScenarioResponse;
import com.server.gummymurderer.domain.dto.scenario.MakeScenarioRequest;
import com.server.gummymurderer.domain.dto.scenario.MakeScenarioResponse;
import com.server.gummymurderer.domain.dto.scenario.NpcInfo;
import com.server.gummymurderer.domain.entity.*;
import com.server.gummymurderer.exception.AppException;
import com.server.gummymurderer.exception.ErrorCode;
import com.server.gummymurderer.repository.GameAlibiRepository;
import com.server.gummymurderer.repository.GameNpcRepository;
import com.server.gummymurderer.repository.GameScenarioRepository;
import com.server.gummymurderer.repository.GameSetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

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

    @Transactional
    public MakeScenarioResponse makeScenario(MakeScenarioRequest request, Member loginMember) throws JsonProcessingException {

        // 일치하는 게임이 없을경우 에러 발생
        GameSet foundGameSet = gameSetRepository.findByGameSetNoAndMember(request.getGameSetNo(), loginMember)
                .orElseThrow(() -> new AppException(ErrorCode.GAME_SET_NOT_FOUND));

        // AI에게 시나리오 생성 요청보내는 로직
        List<NpcInfo> aliveGameNpcList = gameNpcRepository.findAllAliveResidentNpcInfoByGameSetNo(foundGameSet.getGameSetNo());
        String murderName = gameNpcRepository.findMurderByGameSetNo(foundGameSet.getGameSetNo());
        log.info("🤖 머더러 이름 : {}", murderName);
        String secretKey = "";
        log.info("🤖 secret key : {}", secretKey);
        Long day = foundGameSet.getGameStatus();
        log.info("🤖 day : {} 일차", day);
        String previousStory = foundGameSet.getGameSummary();
        log.info("🤖 previousStory : {} ", previousStory);

        String url = "http://221.163.19.218:9090/api/scenario/generate_victim";

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

        log.info("🤖 result victim : {}", result.getAnswer().getVictim());
        log.info("🤖 result token : {}", result.getTokens().getTotalTokens());
        log.info("🤖 result : {}", result.getAnswer().getDailySummary());

        GameScenario savedGameScenario = gameScenarioRepository.save(new GameScenario(result, foundGameSet));

        // Alibi 정보를 GameAlibi에 저장
        for (AlibiDTO alibiDTO : result.getAnswer().getAlibis()) {

            GameNpc gameNpc = gameNpcRepository.findByGameNpcNo(alibiDTO.getGameNpcNo())
                    .orElseThrow(() -> new AppException(ErrorCode.NPC_NOT_FOUND));

            GameAlibi gameAlibi = alibiDTO.toEntity(savedGameScenario, gameNpc);
            gameAlibiRepository.save(gameAlibi);
        }

        return new MakeScenarioResponse(savedGameScenario);
    }
}
