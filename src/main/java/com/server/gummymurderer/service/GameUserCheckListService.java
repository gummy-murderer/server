package com.server.gummymurderer.service;

import com.server.gummymurderer.domain.dto.gameUserCheckList.CheckListSaveRequest;
import com.server.gummymurderer.domain.dto.gameUserCheckList.CheckListSaveResponse;
import com.server.gummymurderer.domain.entity.GameNpc;
import com.server.gummymurderer.domain.entity.GameSet;
import com.server.gummymurderer.domain.entity.GameUserCheckList;
import com.server.gummymurderer.exception.AppException;
import com.server.gummymurderer.exception.ErrorCode;
import com.server.gummymurderer.repository.GameNpcRepository;
import com.server.gummymurderer.repository.GameSetRepository;
import com.server.gummymurderer.repository.GameUserCheckListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameUserCheckListService {

    private final GameUserCheckListRepository gameUserChecklistRepository;
    private final GameNpcRepository gameNpcRepository;
    private final GameSetRepository gameSetRepository;

    public CheckListSaveResponse saveAndReturnCheckList(CheckListSaveRequest request) {

        GameSet gameSet = gameSetRepository.findByGameSetNo(request.getGameSetNo())
                .orElseThrow(() -> new AppException(ErrorCode.GAME_SET_NOT_FOUND));

        GameNpc gameNpc = gameNpcRepository.findByGameNpcNoAndGameSet(request.getGameNpcNo(), gameSet)
                .orElseThrow(() -> new AppException(ErrorCode.NPC_NOT_FOUND));

        GameUserCheckList gameUserCheckList = CheckListSaveRequest.toEntity(request, gameNpc);

        gameUserCheckList = gameUserChecklistRepository.save(gameUserCheckList);

        return new CheckListSaveResponse(gameUserCheckList.getMark(), gameUserCheckList.getCheckJob());
    }
}
