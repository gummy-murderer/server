package com.server.gummymurderer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.gummymurderer.domain.dto.alibi.AlibiDTO;
import com.server.gummymurderer.domain.dto.game.*;
import com.server.gummymurderer.domain.dto.gameNpc.GameNpcDTO;
import com.server.gummymurderer.domain.dto.gameUserCheckList.CheckListSaveRequest;
import com.server.gummymurderer.domain.dto.gameUserCheckList.CheckListSaveResponse;
import com.server.gummymurderer.domain.dto.gameUserCustom.GameUserCustomSaveRequest;
import com.server.gummymurderer.domain.dto.scenario.MakeScenarioResponse;
import com.server.gummymurderer.domain.entity.*;
import com.server.gummymurderer.domain.enum_class.GameResult;
import com.server.gummymurderer.domain.enum_class.GameStatus;
import com.server.gummymurderer.domain.enum_class.VoteResult;
import com.server.gummymurderer.exception.AppException;
import com.server.gummymurderer.exception.ErrorCode;
import com.server.gummymurderer.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class GameService {

    private final NpcRepository npcRepository;
    private final GameSetRepository gameSetRepository;
    private final GameNpcRepository gameNpcRepository;
    private final GameVoteEventRepository gameVoteEventRepository;
    private final GameScenarioRepository gameScenarioRepository;
    private final GameUserCheckListRepository gameUserCheckListRepository;
    private final GameAlibiRepository gameAlibiRepository;
    private final MemberRepository memberRepository;
    private final GameUserCustomRepository gameUserCustomRepository;

    private final GameUserCheckListService gameUserCheckListService;
    private final GameUserCustomService gameUserCustomService;

    public SecretKeyValidationResponse validationSecretKey(Member loginMember, SecretKeyValidationRequest request) throws JsonProcessingException {

        log.info("🐻secretKey 검증 시작");

        Member member = memberRepository.findByNickname(loginMember.getNickname())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_ACCOUNT));

        String url = "http://ec2-3-39-251-140.ap-northeast-2.compute.amazonaws.com:80/api/etc/secret_key_validation";

        Map<String, String> requestData = new HashMap<>();
        requestData.put("secretKey", request.getSecretKey());

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonRequest = objectMapper.writeValueAsString(requestData);
        log.info("🐻jsonRequest : {}", jsonRequest);

        WebClient webClient = WebClient.create();

        SecretKeyValidationResponse result;
        try {
            result = webClient
                    .post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(jsonRequest))
                    .retrieve()
                    .bodyToMono(SecretKeyValidationResponse.class)
                    .doOnNext(response -> log.info("🐻AI server response: {}", response)) // AI 서버 응답 로그
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("🐻Error from AI server: {}", ex.getResponseBodyAsString()); // AI 서버 에러 로그
            if (400 <= ex.getRawStatusCode() && ex.getRawStatusCode() < 500) {
                String errorBody = ex.getResponseBodyAsString();
                String detail = objectMapper.readTree(errorBody).get("detail").asText();
                result = new SecretKeyValidationResponse(null, detail, false);
            } else {
                throw ex;
            }
        }

        log.info("🐻result : {}", result);

        return result;
    }


    @Transactional
    public StartGameResponse startGame(Member loginMember) {

        log.info("🐻Game Start 시작");

        int saveGameCount = gameSetRepository.findGameSetsByMember(loginMember).size();
        log.info("🤖 저장된 게임 갯수 : {}", saveGameCount);

        // 게임 슬롯 최대 3개 저장
//        if (saveGameCount >= 3) {
//            throw new AppException(ErrorCode.SAVED_GAME_FULL);
//        }

        log.info("🤖 계정명 : " + loginMember.getAccount());

        // Game Set 구성
        GameSet gameSet = GameSet.builder()
                .gameStatus(GameStatus.GAME_START)
                .gameResult(GameResult.IN_PROGRESS)
                .gameDay(1)
                .gameSummary("")
                .gameToken(0)
                .member(loginMember)
                .build();

        GameSet savedGameSet = gameSetRepository.saveAndFlush(gameSet);

        List<Npc> npcList = npcRepository.findRandom9Npc();

        List<GameNpc> gameNpcList = new ArrayList<>();

        for (int i = 0; i < npcList.size(); i++) {
            Npc npc = npcList.get(i);
            String npcJob = (i < npcList.size() - 1) ? "Resident" : "Murderer";
            gameNpcList.add(createGameNpc(npc, npcJob, savedGameSet));
        }

        gameNpcRepository.saveAll(gameNpcList);

        return StartGameResponse.builder()
                .gameSetNo(savedGameSet.getGameSetNo())
                .build();
    }

    @Transactional
    public GameNpc createGameNpc(Npc npc, String npcJob, GameSet gameSet) {
        return new GameNpc(npc, npcJob, gameSet);
    }

    @Transactional
    public SaveGameResponse gameSave(Member loginMember, SaveGameRequest request) {

        log.info("🐻Game Save 시작");

        GameSet gameSet = gameSetRepository.findByGameSetNoAndMember(request.getGameSetNo(), loginMember)
                .orElseThrow(() -> new AppException(ErrorCode.GAME_SET_NOT_FOUND));

        // 투표가 이루어진 경우에만 투표 이벤트 처리
        if (request.getVoteNpcName() != null && request.getVoteResult() != null && request.getVoteNightNumber() != 0) {
            // 투표된 NPC 찾기
            GameNpc voteGameNpc = gameNpcRepository.findByNpcNameAndGameSet(request.getVoteNpcName(), gameSet)
                    .orElseThrow(() -> new AppException(ErrorCode.GAME_SET_NOT_FOUND));

            log.info("🐻투표된 npc : {}", voteGameNpc);

            // NPC 상태 dead로 변경
            voteGameNpc.voteEvent();

            // 투표 이벤트 생성 및 저장
            GameVoteEvent gameVoteEvent = new GameVoteEvent(request, gameSet);
            gameVoteEventRepository.save(gameVoteEvent);

            log.info("🐻투표 이벤트 저장 No : {}", gameVoteEvent.getGameVoteEventNo());
            log.info("🐻투표 이벤트 저장 지목 npc : {}", gameVoteEvent.getVoteNpcName());
            log.info("🐻투표 이벤트 저장 투표 결과 : {}", gameVoteEvent.getVoteResult());

            // 투표 결과가 FOUND인 경우 게임 종료 및 성공
            if (VoteResult.valueOf(request.getVoteResult()) == VoteResult.FOUND) {
                gameSet.endGameStatus();
                gameSet.gameSuccess();
            }
        }

        // 체크 리스트 저장
        CheckListSaveRequest checkListSaveRequest = new CheckListSaveRequest();
        checkListSaveRequest.setGameSetNo(request.getGameSetNo());
        checkListSaveRequest.setCheckList(request.getCheckList());
        gameUserCheckListService.saveAndReturnCheckList(checkListSaveRequest);

        // custom 저장
        if (request.getCustom() != null) {
            request.getCustom().setGameSetNo(request.getGameSetNo());
            gameUserCustomService.saveCustom(loginMember, request.getCustom());
        }

        // 게임 상태가 GAME_START 이면 GAME_PROGRESS로 변경
        if (gameSet.getGameStatus() == GameStatus.GAME_START) {
            gameSet.gameStatusChange();
        }

        gameSet.updateGameDay();
        gameSetRepository.save(gameSet);

        log.info("🐻 Game Save 완료");

        return new SaveGameResponse(gameSet);
    }

    public LoadGameResponse gameLoad(Member loginMember, Long gameSetNo) {

        log.info("🐻Game Load 시작");

        GameSet gameSet = gameSetRepository.findByGameSetNoAndMember(gameSetNo, loginMember)
                .orElseThrow(() -> new AppException(ErrorCode.GAME_SET_NOT_FOUND));

        log.info("🐻Load GameSetNo : {}", gameSet.getGameSetNo());

        GameUserCustom gameUserCustom = gameUserCustomRepository.findByGameSet(gameSet).orElse(null);

        // GameSet을 LoginGameSetDTO로 변환
        LoginGameSetDTO gameSetDTO = new LoginGameSetDTO(gameSet, gameUserCustom);

        // GameScenario를 MakeScenarioResponse로 변환
        GameScenario gameScenario = gameScenarioRepository.findTopByGameSetOrderByScenarioNoDesc(gameSet)
                .orElseThrow(() -> new AppException(ErrorCode.SCENARIO_NOT_FOUND));

        log.info("🐻Load Game Scenario DailySummary : {}", gameScenario.getDailySummary());

        // 해당 게임의 npc list
        List<GameNpc> gameNpcs = gameNpcRepository.findAllByGameSet(gameSet);

        List<GameNpcDTO> npcList = new ArrayList<>();
        for (GameNpc gameNpc : gameNpcs) {
            GameNpcDTO dto = new GameNpcDTO(gameNpc);
            npcList.add(dto);
        }

        MakeScenarioResponse scenarioResponse = MakeScenarioResponse.of(gameScenario, npcList);

        // 로그인 한 user의 GameSet에 해당하는 checkList
        List<GameUserCheckList> gameUserCheckLists = gameUserCheckListRepository.findByGameNpc_GameSet(gameSet);
        List<CheckListSaveResponse> checkList = gameUserCheckLists.stream()
                .map(CheckListSaveResponse::of)
                .toList();

        // GameSet에 해당하는 Alibi
        List<GameAlibi> alibis = new ArrayList<>();
        for (GameNpc gameNpc : gameNpcs) {
            Optional<GameAlibi> optionalGameAlibi = gameAlibiRepository.findByGameScenarioAndGameNpc(gameScenario, gameNpc);
            optionalGameAlibi.ifPresent(alibis::add);
        }
        List<AlibiDTO> alibiDTOList = alibis.stream()
                .map(AlibiDTO::of)
                .toList();

        // 죽은 npc와 죽은 장소
        String deadNpc = scenarioResponse.getVictim();
        String deadPlace = scenarioResponse.getCrimeScene();

        log.info("🐻Game Load deadNpc : {}", deadNpc);
        log.info("🐻Game Load deadPlace : {}", deadPlace);

        log.info("🐻Game Load 완료");

        return LoadGameResponse.of(gameSetDTO, deadNpc, deadPlace, checkList, alibiDTOList, scenarioResponse);
    }

    @Transactional
    public EndGameResponse gameEnd(Member loginMember, EndGameRequest request) {

        log.info("🐻Game End 시작");

        GameSet gameSet = gameSetRepository.findByGameSetNoAndMember(request.getGameSetNo(), loginMember)
                .orElseThrow(() -> new AppException(ErrorCode.GAME_SET_NOT_FOUND));

        log.info("🐻종료될 GameSetNo : {}", request.getGameSetNo());

        gameSet.endGameStatus();

        log.info("🐻종료 요청 게임 상태 : {}", gameSet.getGameStatus());

        if ("FAILURE".equals(request.getResultMessage())) {
            gameSet.gameFailed();
        } else {
            throw new AppException(ErrorCode.INVALID_RESULT_MESSAGE);
        }

        log.info("🐻Game End 완료");

        return new EndGameResponse(request.getResultMessage());
    }
}
