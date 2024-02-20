package com.server.gummymurderer.service;

import com.server.gummymurderer.domain.dto.game.*;
import com.server.gummymurderer.domain.dto.gameNpc.GameNpcDTO;
import com.server.gummymurderer.domain.dto.gameUserCheckList.CheckListRequest;
import com.server.gummymurderer.domain.dto.gameUserCheckList.CheckListSaveRequest;
import com.server.gummymurderer.domain.entity.*;
import com.server.gummymurderer.domain.enum_class.GameResult;
import com.server.gummymurderer.domain.enum_class.GameStatus;
import com.server.gummymurderer.exception.AppException;
import com.server.gummymurderer.exception.ErrorCode;
import com.server.gummymurderer.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    private final GameUserCheckListService gameUserCheckListService;

    @Transactional
    public StartGameResponse startGame(Member loginMember) {

        int saveGameCount = gameSetRepository.findGameSetsByMember(loginMember).size();
        log.info("🤖 저장된 게임 갯수 : {}", saveGameCount);

//        if (saveGameCount > 2) {
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

        List<Npc> npcList = npcRepository.findRandom5Npc();

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

        GameSet gameSet = gameSetRepository.findByGameSetNoAndMember(request.getGameSetNo(), loginMember)
                .orElseThrow(() -> new AppException(ErrorCode.GAME_SET_NOT_FOUND));

        // 투표된 NPC 찾기
        GameNpc voteGameNpc = gameNpcRepository.findByNpcNameAndGameSet(request.getVoteNpcName(), gameSet)
                .orElseThrow(() -> new AppException(ErrorCode.GAME_SET_NOT_FOUND));

        // NPC 상태 dead로 변경
        voteGameNpc.voteEvent();

        // 투표 이벤트 생성 및 저장
        GameVoteEvent gameVoteEvent = new GameVoteEvent(request, gameSet);
        gameVoteEventRepository.save(gameVoteEvent);

        // 체크 리스트 저장
        CheckListSaveRequest checkListSaveRequest = new CheckListSaveRequest();
        checkListSaveRequest.setGameSetNo(request.getGameSetNo());
        checkListSaveRequest.setCheckList(request.getCheckList());
        gameUserCheckListService.saveAndReturnCheckList(checkListSaveRequest);

        // 게임 상태가 GAME_START 이면 GAME_PROGRESS로 변경
        if (gameSet.getGameStatus() == GameStatus.GAME_START) {
            gameSet.gameStatusChange();
        }

        gameSet.updateGameDay();
        gameSetRepository.save(gameSet);

        return new SaveGameResponse(gameSet);
    }

    @Transactional
    public EndGameResponse gameEnd(Member loginMember, EndGameRequest request) {
        GameSet gameSet = gameSetRepository.findByGameSetNoAndMember(request.getGameSetNo(), loginMember)
                .orElseThrow(() -> new AppException(ErrorCode.GAME_SET_NOT_FOUND));

        gameSet.endGameStatus();

        return new EndGameResponse(request.getResultMessage());
    }
}
