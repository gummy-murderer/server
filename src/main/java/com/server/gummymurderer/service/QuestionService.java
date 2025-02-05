package com.server.gummymurderer.service;

import com.server.gummymurderer.configuration.jwt.JwtProvider;
import com.server.gummymurderer.domain.dto.question.*;
import com.server.gummymurderer.domain.entity.GameSet;
import com.server.gummymurderer.domain.entity.Member;
import com.server.gummymurderer.domain.entity.QuestionAnswer;
import com.server.gummymurderer.domain.enum_class.KeyWordType;
import com.server.gummymurderer.exception.AppException;
import com.server.gummymurderer.exception.ErrorCode;
import com.server.gummymurderer.repository.GameSetRepository;
import com.server.gummymurderer.repository.QuestionAnswerRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionService {

    private final GameSetRepository gameSetRepository;
    private final QuestionAnswerRepository questionAnswerRepository;
    private final JwtProvider jwtProvider;

    @Value("${ai.url}")
    private String aiUrl;

    public QuestionAnswerResponse getAnswer(Member loginMember, QuestionAnswerRequest request, HttpServletRequest httpServletRequest) {

        log.info("🐻Question Answer 시작");

        String authHeader = httpServletRequest.getHeader("Authorization");

        // 요청에서 받은 Authorization 헤더 출력
        log.info("🐻Received Authorization header: {}", authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // 토큰 부분만 출력
        String token = authHeader.substring(7);
        log.info("🐻Extracted token: {}", token);

        if (loginMember == null) {
            log.error("🐻loginMember is null");
            throw new AppException(ErrorCode.UNAUTHORIZED);
        } else {
            if (loginMember.getNickname() == null) {
                log.error("🐻loginMember nickname is null");
                throw new AppException(ErrorCode.UNAUTHORIZED);
            }
        }

        // 토큰 유효성 검사 결과 출력
        boolean isValid = jwtProvider.validateToken(authHeader);
        log.info("🐻Token validation result: {}", isValid);

        if (!isValid) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        Optional<GameSet> optionalGameSet = gameSetRepository.findByGameSetNo(request.getGameSetNo());

        if (optionalGameSet.isEmpty()) {
            throw new AppException(ErrorCode.GAME_NOT_FOUND);
        }

        log.info("🐻user-npc Question Answer unity 통신 완료");

        try {
            return sendAIToAnswer(request);
        } catch (Exception e) {
            log.error("🐻AI 통신 실패 : ", e);
            throw e;
        }
    }


    private QuestionAnswerResponse sendAIToAnswer(QuestionAnswerRequest request) {

        log.info("🐻Question Answer AI 통신 시작");

        String aiServerUrl = aiUrl + "/api/v2/in-game/generate-answer";
        WebClient webClient = WebClient.builder().baseUrl(aiServerUrl).build();

        GameSet gameSet = gameSetRepository.findByGameSetNo(request.getGameSetNo())
                .orElseThrow(() -> new AppException(ErrorCode.GAME_SET_NOT_FOUND));

        // AI 서버에 보낼 요청 객체 생성
        AIQuestionAnswerRequest aiQuestionAnswerRequest = new AIQuestionAnswerRequest();
        aiQuestionAnswerRequest.setGameNo(request.getGameSetNo());
        aiQuestionAnswerRequest.setNpcName(request.getNpcName());
        aiQuestionAnswerRequest.setKeyWord(request.getKeyWord() != null ? request.getKeyWord() : "");
        aiQuestionAnswerRequest.setKeyWordType(request.getKeyWord() != null ? request.getKeyWordType() : "");

        // 요청 객체 로그 출력
        log.info("🐻Sending request to AI server: {}", aiQuestionAnswerRequest);

        // AI 서버로 요청
        QuestionAnswerResponse aiResponse = webClient.post()
                .uri(aiServerUrl)
                .bodyValue(aiQuestionAnswerRequest)
                .retrieve()
                .bodyToMono(QuestionAnswerResponse.class)
                .onErrorResume(e -> {
                    log.error("🐻AI 통신 실패 : ", e);
                    throw new AppException(ErrorCode.AI_INTERNAL_SERVER_ERROR);
                })
                .block();

        // 질문 없이 바로 답변 저장
        QuestionAnswer questionAnswer = QuestionAnswer.builder()
                .gameSetNo(request.getGameSetNo())
                .npcName(request.getNpcName())
                .keyWord(request.getKeyWord())
                .keyWordType(KeyWordType.valueOf(request.getKeyWordType()))
                .answerText(aiResponse.getResponse())
                .build();

        questionAnswerRepository.save(questionAnswer);

        log.info("🐻Question Answer AI 통신 완료");

        return aiResponse;
    }

}
