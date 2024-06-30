package com.server.gummymurderer.service;

import com.server.gummymurderer.domain.dto.gameNpcCustom.GameNpcCustomSaveRequest;
import com.server.gummymurderer.domain.dto.gameNpcCustom.GameNpcCustomSaveResponse;
import com.server.gummymurderer.domain.dto.gameNpcCustom.NpcCustomInfo;
import com.server.gummymurderer.domain.entity.GameNpc;
import com.server.gummymurderer.domain.entity.GameNpcCustom;
import com.server.gummymurderer.domain.entity.GameSet;
import com.server.gummymurderer.exception.AppException;
import com.server.gummymurderer.exception.ErrorCode;
import com.server.gummymurderer.repository.GameNpcCustomRepository;
import com.server.gummymurderer.repository.GameNpcRepository;
import com.server.gummymurderer.repository.GameSetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameNpcCustomService {

    private final GameSetRepository gameSetRepository;
    private final GameNpcRepository gameNpcRepository;
    private final GameNpcCustomRepository gameNpcCustomRepository;

    public GameNpcCustomSaveResponse npcCustomSave(GameNpcCustomSaveRequest request) {

        log.info("🐻GameNpc custom 저장 시작");

        GameSet gameSet = gameSetRepository.findByGameSetNo(request.getGameSetNo())
                .orElseThrow(() -> new AppException(ErrorCode.GAME_SET_NOT_FOUND));

        GameNpcCustomSaveResponse response = null;

        List<NpcCustomInfo> npcCustomInfos = request.getNpcCustomInfos();

        for (int i = 0; i < npcCustomInfos.size(); i++) {

            NpcCustomInfo npcInfo = npcCustomInfos.get(i);

            log.info("🐻 처리 중인 NPC - 순서: {}, NPC 이름: {}", i + 1, npcInfo.getNpcName());

            GameNpc gameNpc = gameNpcRepository.findByNpcNameAndGameSet_GameSetNo(npcInfo.getNpcName(), request.getGameSetNo())
                    .orElseThrow(() -> new AppException(ErrorCode.NPC_NOT_FOUND));

            GameNpcCustom gameNpcCustom = npcInfo.toEntity(gameSet, gameNpc);

            gameNpcCustomRepository.save(gameNpcCustom);

            response = new GameNpcCustomSaveResponse(gameNpcCustom);
        }

        log.info("🐻GameNpc custom 저장 완료");

        return response;
    }
}
