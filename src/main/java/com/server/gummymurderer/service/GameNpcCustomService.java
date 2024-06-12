package com.server.gummymurderer.service;

import com.server.gummymurderer.domain.dto.gameNpcCustom.GameNpcCustomSaveRequest;
import com.server.gummymurderer.domain.dto.gameNpcCustom.GameNpcCustomSaveResponse;
import com.server.gummymurderer.domain.entity.GameNpc;
import com.server.gummymurderer.domain.entity.GameNpcCustom;
import com.server.gummymurderer.domain.entity.GameSet;
import com.server.gummymurderer.domain.entity.Member;
import com.server.gummymurderer.exception.AppException;
import com.server.gummymurderer.exception.ErrorCode;
import com.server.gummymurderer.repository.GameNpcCustomRepository;
import com.server.gummymurderer.repository.GameNpcRepository;
import com.server.gummymurderer.repository.GameSetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameNpcCustomService {

    private final GameSetRepository gameSetRepository;
    private final GameNpcRepository gameNpcRepository;
    private final GameNpcCustomRepository gameNpcCustomRepository;

    public GameNpcCustomSaveResponse npcCustomSave(Member loginMember, GameNpcCustomSaveRequest request) {

        log.info("🐻GameNpc custom 저장 시작");

        GameSet gameSet = gameSetRepository.findByGameSetNoAndMember(request.getGameSetNo(), loginMember)
                .orElseThrow(() -> new AppException(ErrorCode.GAME_SET_NOT_FOUND));

        GameNpc gameNpc = gameNpcRepository.findByNpcNameAndGameSet_GameSetNo(request.getNpcName(), request.getGameSetNo())
                .orElseThrow(() -> new AppException(ErrorCode.NPC_NOT_FOUND));

        GameNpcCustom gameNpcCustom = request.toEntity(gameSet, gameNpc);

        gameNpcCustomRepository.save(gameNpcCustom);

        log.info("🐻GameNpc custom 저장 완료");

        return new GameNpcCustomSaveResponse(gameNpcCustom);
    }
}
