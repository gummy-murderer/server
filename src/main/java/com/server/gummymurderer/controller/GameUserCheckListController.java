package com.server.gummymurderer.controller;

import com.server.gummymurderer.domain.dto.gameUserCheckList.CheckListSaveRequest;
import com.server.gummymurderer.domain.dto.gameUserCheckList.CheckListSaveResponse;
import com.server.gummymurderer.exception.Response;
import com.server.gummymurderer.service.GameUserCheckListService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/checklist")
@RequiredArgsConstructor
public class GameUserCheckListController {

    private final GameUserCheckListService gameUserCheckListService;

    @PostMapping
    public Response<List<CheckListSaveResponse>> saveCheckList(@RequestBody CheckListSaveRequest request) {

        List<CheckListSaveResponse> response = gameUserCheckListService.saveAndReturnCheckList(request);
        return Response.success(response);
    }
}