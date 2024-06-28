package com.server.gummymurderer.controller;

import com.server.gummymurderer.domain.dto.gameNpcCustom.GameNpcCustomSaveRequest;
import com.server.gummymurderer.domain.dto.gameNpcCustom.GameNpcCustomSaveResponse;
import com.server.gummymurderer.domain.entity.Member;
import com.server.gummymurderer.exception.Response;
import com.server.gummymurderer.service.CustomUserDetails;
import com.server.gummymurderer.service.GameNpcCustomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/npc/custom")
@RequiredArgsConstructor
public class GameNpcCustomController {

    private final GameNpcCustomService gameNpcCustomService;

    @PostMapping("/save")
    public Response<GameNpcCustomSaveResponse> saveNpcCustom(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody List<GameNpcCustomSaveRequest> requests) {

        Member loginMember = customUserDetails.getMember();
        GameNpcCustomSaveResponse response = gameNpcCustomService.npcCustomSave(loginMember, requests);

        return Response.success(response);
    }
}
