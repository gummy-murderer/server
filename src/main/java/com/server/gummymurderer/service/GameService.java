package com.server.gummymurderer.service;

import com.server.gummymurderer.domain.dto.game.SaveGameRequest;
import com.server.gummymurderer.domain.dto.game.SaveGameResponse;
import com.server.gummymurderer.domain.dto.game.StartGameResponse;
import com.server.gummymurderer.domain.entity.*;
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

    @Transactional
    public StartGameResponse startGame(Member loginMember) {

        log.info("🤖 계정명 : " + loginMember.getAccount());

        // Game Set 구성
        GameSet gameSet = GameSet.builder()
                .gameStatus(0L)
                .gameSummary("")
                .gameToken(0)
                .member(loginMember)
                .build();

        GameSet savedGameSet = gameSetRepository.saveAndFlush(gameSet);

        List<Npc> npcList = npcRepository.findRandom7Npc();

        log.info(String.valueOf(npcList.get(0)));
        List<GameNpc> gameNpcList = new ArrayList<>();

        for (int i = 0; i < npcList.size(); i++) {
            Npc npc = npcList.get(i);
            String npcJob = (i < npcList.size() -1) ? "Resident" : "Murderer";
            gameNpcList.add(createGameNpc(npc, npcJob, savedGameSet));
        }

        List<GameNpc> savedGameNpcList = gameNpcRepository.saveAll(gameNpcList);


        //AI에 Scenario 요청하는 로직 넣어야함.
        GameScenario gameScenario = GameScenario.builder().build();

        GameScenario savedGameScenario = gameScenarioRepository.save(gameScenario);

        return new StartGameResponse(savedGameSet,savedGameScenario, savedGameNpcList);
    }

    @Transactional
    public GameNpc createGameNpc(Npc npc, String npcJob, GameSet gameSet) {
        return new GameNpc(npc, npcJob, gameSet);
    }

    @Transactional
    public SaveGameResponse gameSave(Member loginMember, SaveGameRequest request) {

        GameSet gameSet = gameSetRepository.findByGameSetNoAndMember(request.getGameSetNo(), loginMember)
                .orElseThrow(() -> new AppException(ErrorCode.GAME_SET_NOT_FOUND));
        Long gameDate = gameVoteEventRepository.countAllByGameSet(gameSet);
        gameSet.updateGameStatus(gameDate + 1 );


        GameVoteEvent gameVoteEvent = new GameVoteEvent(request, gameSet);
        String npcName = gameVoteEvent.getVoteNpcName();
        GameNpc voteGameNpc = gameNpcRepository.findByNpcName(npcName)
                .orElseThrow(() -> new AppException(ErrorCode.NPC_NOT_FOUND));

        // npc 상태 dead 로 변경
        voteGameNpc.voteEvent();
        gameVoteEventRepository.save(gameVoteEvent);

        return new SaveGameResponse(gameSet,voteGameNpc,gameVoteEvent);
    }
}
