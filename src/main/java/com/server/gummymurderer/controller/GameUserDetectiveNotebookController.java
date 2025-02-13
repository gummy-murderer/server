package com.server.gummymurderer.controller;

import com.server.gummymurderer.domain.dto.gameUserDetectiveNotebook.DetectiveNotebookSaveRequest;
import com.server.gummymurderer.domain.dto.gameUserDetectiveNotebook.DetectiveNotebookSaveResponse;
import com.server.gummymurderer.domain.entity.Member;
import com.server.gummymurderer.exception.Response;
import com.server.gummymurderer.service.CustomUserDetails;
import com.server.gummymurderer.service.GameUserDetectiveNotebookService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notebook")
@RequiredArgsConstructor
public class GameUserDetectiveNotebookController {

    private final GameUserDetectiveNotebookService gameUserDetectiveNotebookService;

    @PostMapping
    public Response<List<DetectiveNotebookSaveResponse>> saveDetectiveNote(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody DetectiveNotebookSaveRequest request) {

        Member loginMember = customUserDetails.getMember();
        List<DetectiveNotebookSaveResponse> response = gameUserDetectiveNotebookService.saveOrUpdateNotebook(loginMember, request);
        return Response.success(response);
    }

}