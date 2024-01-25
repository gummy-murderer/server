package com.server.gummymurderer.service;

import com.server.gummymurderer.domain.dto.gameUserCheckList.CheckListRequest;
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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameUserCheckListService {

    private final GameUserCheckListRepository gameUserChecklistRepository;
    private final GameNpcRepository gameNpcRepository;
    private final GameSetRepository gameSetRepository;

    public List<CheckListSaveResponse> saveAndReturnCheckList(CheckListSaveRequest request) {

        GameSet gameSet = gameSetRepository.findByGameSetNo(request.getGameSetNo())
                .orElseThrow(() -> new AppException(ErrorCode.GAME_SET_NOT_FOUND));

        List<CheckListSaveResponse> responses = new ArrayList<>();

        for (CheckListRequest checkListRequest : request.getCheckList()) {

            GameNpc gameNpc = gameNpcRepository.findByNpcNameAndGameSet(checkListRequest.getNpcName(), gameSet)
                    .orElseThrow(() -> new AppException(ErrorCode.NPC_NOT_FOUND));

            GameUserCheckList gameUserCheckList = checkListRequest.toEntity(gameNpc);

            gameUserCheckList = gameUserChecklistRepository.save(gameUserCheckList);

            responses.add(CheckListSaveResponse.of(gameUserCheckList));

        }
        return responses;
    }
}