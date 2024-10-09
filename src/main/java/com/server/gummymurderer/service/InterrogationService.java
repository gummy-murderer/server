package com.server.gummymurderer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.server.gummymurderer.configuration.jwt.JwtProvider;
import com.server.gummymurderer.domain.dto.interrogation.InterrogationProceedRequest;
import com.server.gummymurderer.domain.dto.interrogation.InterrogationProceedResponse;
import com.server.gummymurderer.domain.dto.interrogation.InterrogationStartRequest;
import com.server.gummymurderer.domain.dto.interrogation.InterrogationStartResponse;
import com.server.gummymurderer.domain.entity.GameSet;
import com.server.gummymurderer.domain.entity.Interrogation;
import com.server.gummymurderer.domain.entity.InterrogationDialogue;
import com.server.gummymurderer.domain.entity.Member;
import com.server.gummymurderer.exception.AppException;
import com.server.gummymurderer.exception.ErrorCode;
import com.server.gummymurderer.repository.GameSetRepository;
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
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class InterrogationService {

    private final InterrogationRepository interrogationRepository;
    private final GameSetRepository gameSetRepository;
    private final JwtProvider jwtProvider;

    @Value("${ai.url}")
    private String aiUrl;

    public InterrogationStartResponse interrogationStart(InterrogationStartRequest request, Member loginMember, HttpServletRequest httpServletRequest) throws JsonProcessingException {

        log.info("🐻Interrogation Start 시작");

        validateUser(loginMember, httpServletRequest);

        GameSet gameSet = gameSetRepository.findByGameSetNo(request.getGameSetNo())
                .orElseThrow(() -> new AppException(ErrorCode.GAME_SET_NOT_FOUND));

        String aiServerUrl =  aiUrl + "/api/v2/interrogation/new";
        WebClient webClient = WebClient.builder().baseUrl(aiServerUrl).build();

        Map<String, Object> requestData = new HashMap<>();
        requestData.put("gameNo", request.getGameSetNo());
        requestData.put("npcName", request.getNpcName());
        requestData.put("weapon", request.getWeapon());

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonRequest = objectMapper.writeValueAsString(requestData);
        log.info("🐻jsonRequest : {}", jsonRequest);

        InterrogationStartResponse response = webClient
                .post()
                .uri(aiServerUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(jsonRequest))
                .retrieve()
                .bodyToMono(InterrogationStartResponse.class)
                .block();

        Interrogation interrogation = request.toEntity(gameSet);
        interrogationRepository.save(interrogation);

        return response;
    }

    public InterrogationProceedResponse interrogationProceed (InterrogationProceedRequest request) throws JsonProcessingException {

        log.info("🐻Interrogation conversation 시작");

        log.info("🐻 unity request : {}", request);

        GameSet gameSet = gameSetRepository.findByGameSetNo(request.getGameSetNo())
                .orElseThrow(() -> new AppException(ErrorCode.GAME_SET_NOT_FOUND));

        log.info("🐻 unity request GameSetNo : {}", request.getGameSetNo());

        Interrogation interrogation = interrogationRepository.findByGameSetAndNpcName(gameSet, request.getNpcName())
                .orElseThrow(() -> new AppException(ErrorCode.INTERROGATION_NOT_FOUND));

        log.info("🐻 unity request NpcName : {}", request.getNpcName());

        String aiServerUrl =  aiUrl + "/api/v2/interrogation/conversation";
        WebClient webClient = WebClient.builder().baseUrl(aiServerUrl).build();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode jsonRequest = objectMapper.createObjectNode();
        jsonRequest.put("gameNo", request.getGameSetNo());
        jsonRequest.put("npcName", request.getNpcName());
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

        InterrogationDialogue dialogue = InterrogationDialogue.fromRequest(request.getContent(), response.getResponse(), response.getHeartRate(), interrogation);
        interrogation.addDialogue(dialogue);
        interrogationRepository.save(interrogation);

        log.info("🐻Interrogation conversation 종료");

        return response;

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
