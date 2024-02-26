package com.server.gummymurderer.controller;

import com.server.gummymurderer.domain.dto.gameUserCustom.GameUserCustomSaveRequest;
import com.server.gummymurderer.domain.dto.gameUserCustom.GameUserCustomSaveResponse;
import com.server.gummymurderer.domain.entity.Member;
import com.server.gummymurderer.exception.Response;
import com.server.gummymurderer.service.CustomUserDetails;
import com.server.gummymurderer.service.GameUserCustomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/custom")
@RequiredArgsConstructor
public class GameUserCustomController {

    private final GameUserCustomService gameUserCustomService;

    @PostMapping("/save")
    public Response<GameUserCustomSaveResponse> customSave(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody GameUserCustomSaveRequest request) {

        Member loginMember = customUserDetails.getMember();
        GameUserCustomSaveResponse response = gameUserCustomService.saveCustom(loginMember, request);
        return Response.success(response);
    }
}
