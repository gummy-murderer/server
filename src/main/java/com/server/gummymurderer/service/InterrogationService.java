package com.server.gummymurderer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.server.gummymurderer.configuration.jwt.JwtProvider;
import com.server.gummymurderer.domain.dto.interrogation.InterrogationProceedRequest;
import com.server.gummymurderer.domain.dto.interrogation.InterrogationProceedResponse;
import com.server.gummymurderer.domain.dto.interrogation.InterrogationStartRequest;
import com.server.gummymurderer.domain.entity.*;
import com.server.gummymurderer.domain.enum_class.InterrogationStatus;
import com.server.gummymurderer.domain.enum_class.Language;
import com.server.gummymurderer.exception.AppException;
import com.server.gummymurderer.exception.ErrorCode;
import com.server.gummymurderer.repository.GameNpcRepository;
import com.server.gummymurderer.repository.GameSetRepository;
import com.server.gummymurderer.repository.GameSettingRepository;
import com.server.gummymurderer.repository.InterrogationRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class InterrogationService {

    private final InterrogationRepository interrogationRepository;
    private final GameSetRepository gameSetRepository;
    private final GameSettingRepository gameSettingRepository;
    private final JwtProvider jwtProvider;
    private final GameNpcRepository gameNpcRepository;

    @Value("${ai.url}")
    private String aiUrl;

    public InterrogationProceedResponse interrogationStart(InterrogationStartRequest request, Member loginMember, HttpServletRequest httpServletRequest) throws JsonProcessingException {

        log.info("🐻Interrogation Start 시작");

        validateUser(loginMember, httpServletRequest);

        GameSet gameSet = gameSetRepository.findByGameSetNo(request.getGameSetNo())
                .orElseThrow(() -> new AppException(ErrorCode.GAME_SET_NOT_FOUND));

        Language userLanguage = gameSettingRepository.findByMemberNo(loginMember.getMemberNo())
                .map(GameSetting::getLanguage)
                .orElse(Language.KO);

        String userLangCode = userLanguage.name().toLowerCase();

        String aiServerUrl =  aiUrl + "/api/v2/interrogation/new";
        WebClient webClient = WebClient.builder().baseUrl(aiServerUrl).build();

        // npcName 영어로
        Long gameNo = request.getGameSetNo();
        String npcNameEn = resolveNpcNameEn(gameNo, request.getNpcName());

        log.info("🐻 Interrogation 조회용 npcNameEn = {}", npcNameEn);

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("gameNo", request.getGameSetNo());
        requestData.put("language", userLangCode);
        requestData.put("npcName", npcNameEn);
        requestData.put("murderWeapon", request.getMurderWeapon());
        requestData.put("murderLocation", request.getMurderLocation());
        requestData.put("murderTime", request.getMurderTime());

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonRequest = objectMapper.writeValueAsString(requestData);
        log.info("🐻jsonRequest : {}", jsonRequest);

        InterrogationProceedResponse response = webClient
                .post()
                .uri(aiServerUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(jsonRequest))
                .retrieve()
                .bodyToMono(InterrogationProceedResponse.class)
                .block();

        Interrogation interrogation = request.toEntity(gameSet);
        interrogationRepository.save(interrogation);

        return InterrogationProceedResponse.of(response, userLangCode);
    }

    public InterrogationProceedResponse interrogationProceed (InterrogationProceedRequest request, Member loginMember, HttpServletRequest httpServletRequest) throws JsonProcessingException {

        validateUser(loginMember, httpServletRequest);

        log.info("🐻Interrogation conversation 시작");

        log.info("🐻 unity request : {}", request);

        GameSet gameSet = gameSetRepository.findByGameSetNo(request.getGameSetNo())
                .orElseThrow(() -> new AppException(ErrorCode.GAME_SET_NOT_FOUND));

        log.info("🐻 unity request GameSetNo : {}", request.getGameSetNo());

        Long gameNo = request.getGameSetNo();

        String npcNameEn = resolveNpcNameEn(gameNo, request.getNpcName());
        log.info("🐻 npcNameEn (AI용) = {}", npcNameEn);

        String npcNameKo = gameNpcRepository
                .findByGameSet_GameSetNoAndNpcNameEn(gameNo, npcNameEn)
                .map(GameNpc::getNpcName)
                .orElseThrow(() -> new AppException(ErrorCode.NPC_NOT_FOUND));

        log.info("🐻 npcNameKo (DB 조회용) = {}", npcNameKo);


        List<Interrogation> interrogations =
                interrogationRepository.findByGameSetAndNpcNameOrderByInterrogationNoDesc(gameSet, npcNameKo);

        if (interrogations.isEmpty()) {
            throw new AppException(ErrorCode.INTERROGATION_NOT_FOUND);
        }

        log.info("🐻 unity request NpcName : {}", request.getNpcName());

        // 가장 최근의 Interrogation 사용
        Interrogation interrogation = interrogations.get(0);

        log.info("🐻 선택된 최신 Interrogation : {}", interrogation.getInterrogationNo());

        // 유저 언어 설정
        Language userLanguage = gameSettingRepository.findByMemberNo(loginMember.getMemberNo())
                .map(GameSetting::getLanguage)
                .orElse(Language.KO);

        String userLangCode = userLanguage.name().toLowerCase();

        String aiServerUrl =  aiUrl + "/api/v2/interrogation/conversation";
        WebClient webClient = WebClient.builder().baseUrl(aiServerUrl).build();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonRequest = objectMapper.createObjectNode();
        jsonRequest.put("gameNo", request.getGameSetNo());
        jsonRequest.put("language", userLangCode);
        jsonRequest.put("npcName", npcNameEn);
        jsonRequest.put("content", request.getContent());

        String jsonRequestStr = objectMapper.writeValueAsString(jsonRequest);
        log.info("🐻jsonRequest : {}", jsonRequestStr);

        InterrogationProceedResponse response = webClient
                .post()
                .uri(aiServerUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(jsonRequestStr))
                .retrieve()
                .bodyToMono(InterrogationProceedResponse.class)
                .block();

        InterrogationDialogue dialogue = InterrogationDialogue.fromRequest(
                request.getContent(),
                response.getResponse(),
                response.getHeartRate(),
                interrogation,
                response.isMurderer(),
                InterrogationStatus.valueOf(response.getStatus())
        );
        interrogation.addDialogue(dialogue);
        interrogationRepository.save(interrogation);

        log.info("🐻Interrogation conversation 종료");

        return InterrogationProceedResponse.of(response, userLangCode);

    }

    private String resolveNpcNameEn(Long gameNo, String npcNameInput) {
        return gameNpcRepository
                .findByGameSet_GameSetNoAndNpcNameEn(gameNo, npcNameInput)
                .map(GameNpc::getNpcNameEn)
                .or(() ->
                        gameNpcRepository
                                .findByGameSet_GameSetNoAndNpcName(gameNo, npcNameInput)
                                .map(GameNpc::getNpcNameEn)
                )
                .orElseThrow(() -> new AppException(ErrorCode.NPC_NOT_FOUND));
    }

    private void validateUser(Member loginMember, HttpServletRequest httpServletRequest) {

        String authHeader = httpServletRequest.getHeader("Authorization");

        if (loginMember == null) {
            log.error("🐻loginMember is null");
            throw new AppException(ErrorCode.UNAUTHORIZED);
        } else {
            if (loginMember.getNickname() == null) {
                log.error("🐻loginMember nickname is null");
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
        }

        boolean isValid = jwtProvider.validateToken(authHeader);
        log.info("🐻Token validation result: {}", isValid);

        if (!isValid) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    }



}
