package com.server.gummymurderer.service;

import com.server.gummymurderer.domain.dto.game.StartGameResponse;
import com.server.gummymurderer.domain.entity.Member;
import com.server.gummymurderer.repository.GameScenarioRepository;
import com.server.gummymurderer.repository.GameSetRepository;
import com.server.gummymurderer.repository.MemberRepository;
import com.server.gummymurderer.repository.NpcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class GameService {

    private final NpcRepository npcRepository;
    private final GameSetRepository gameSetRepository;
    private final GameScenarioRepository gameScenarioRepository;
    private final MemberRepository memberRepository;

    public StartGameResponse startGame(Member member) {

        log.info("🤖 계정명 : " + member.getAccount());
        return new StartGameResponse(member.getName());
    }
}
