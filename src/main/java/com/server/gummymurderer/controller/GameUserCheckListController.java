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

@RestController
@RequestMapping("/api/v1/checklist")
@RequiredArgsConstructor
public class GameUserCheckListController {

    private final GameUserCheckListService gameUserCheckListService;

    @PostMapping("/save")
    public Response<CheckListSaveResponse> saveCheckList(@RequestBody CheckListSaveRequest request) {

        gameUserCheckListService.saveCheckList(request);
        CheckListSaveResponse response = new CheckListSaveResponse(request.getMark(), request.getCheckJob());

        return Response.success(response);
    }

}
