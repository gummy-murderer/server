package com.server.gummymurderer.service;

import com.server.gummymurderer.domain.dto.gameUserCustom.GameUserCustomSaveRequest;
import com.server.gummymurderer.domain.dto.gameUserCustom.GameUserCustomSaveResponse;
import com.server.gummymurderer.domain.entity.GameSet;
import com.server.gummymurderer.domain.entity.GameUserCustom;
import com.server.gummymurderer.domain.entity.Member;
import com.server.gummymurderer.exception.AppException;
import com.server.gummymurderer.exception.ErrorCode;
import com.server.gummymurderer.repository.GameSetRepository;
import com.server.gummymurderer.repository.GameUserCustomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameUserCustomService {

    private final GameSetRepository gameSetRepository;
    private final GameUserCustomRepository gameUserCustomRepository;

    // userCustom save
    @Transactional
    public GameUserCustomSaveResponse saveCustom(Member loginMember, GameUserCustomSaveRequest request) {

        log.info("🐻user character custom 저장 시작");

        GameSet gameSet = gameSetRepository.findByGameSetNoAndMember(request.getGameSetNo(), loginMember)
                .orElseThrow(() -> new AppException(ErrorCode.GAME_SET_NOT_FOUND));

        // 1. 기존 데이터가 있는지 체크
        gameUserCustomRepository.findByGameSet(gameSet).ifPresent(existing -> {
            throw new AppException(ErrorCode.DUPLICATE_CUSTOM);
        });

        // 2. 신규 생성
        GameUserCustom gameUserCustom = request.toEntity(request, gameSet);
        gameUserCustomRepository.save(gameUserCustom);

        log.info("🐻user character custom 저장 완료");

        return new GameUserCustomSaveResponse(gameUserCustom);
    }
}
