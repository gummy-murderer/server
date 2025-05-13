package com.server.gummymurderer.controller;

import com.server.gummymurderer.domain.dto.gameSetting.GameSettingRequest;
import com.server.gummymurderer.domain.entity.Member;
import com.server.gummymurderer.exception.Response;
import com.server.gummymurderer.service.CustomUserDetails;
import com.server.gummymurderer.service.GameSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/game/setting")
@RequiredArgsConstructor
public class GameSettingController {

    private final GameSettingService gameSettingService;

    @PostMapping("/save")
    public Response<String> saveGameSetting(@AuthenticationPrincipal CustomUserDetails customUserDetails, GameSettingRequest request) {

        Member loginMember = customUserDetails.getMember();
        String settingSave = gameSettingService.settingSave(loginMember, request);

        return Response.success(settingSave);

    }

}
