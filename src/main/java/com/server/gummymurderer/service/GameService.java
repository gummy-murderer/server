package com.server.gummymurderer.service;

import com.server.gummymurderer.domain.dto.game.StartGameResponse;
import com.server.gummymurderer.domain.entity.GameNpc;
import com.server.gummymurderer.domain.entity.GameSet;
import com.server.gummymurderer.domain.entity.Member;
import com.server.gummymurderer.domain.entity.Npc;
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

    @Transactional
    public StartGameResponse startGame(Member loginMember) {

        log.info("🤖 계정명 : " + loginMember.getAccount());

        // Game Set 구성
        GameSet gameSet = GameSet.builder()
                .gameStatus("start")
                .gameSummary("empty")
                .gameToken(0)
                .member(loginMember)
                .build();

        GameSet savedGameSet = gameSetRepository.saveAndFlush(gameSet);

        List<Npc> npcList = npcRepository.findRandom7Npc();

        log.info(String.valueOf(npcList.get(0)));
        List<GameNpc> gameNpcList = new ArrayList<>();

        for (int i = 0; i < npcList.size(); i++) {
            Npc npc = npcList.get(i);
            String npcJob = (i < npcList.size()/2 -1) ? "Murderer" : "Resident";
            gameNpcList.add(createGameNpc(npc, npcJob, savedGameSet));
        }

        List<GameNpc> savedGameNpcList = gameNpcRepository.saveAll(gameNpcList);


        return new StartGameResponse(savedGameSet,savedGameNpcList);
    }

    public GameNpc createGameNpc(Npc npc, String npcJob, GameSet gameSet) {
        return new GameNpc(npc, npcJob, gameSet);
    }
}
