package com.server.gummymurderer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.server.gummymurderer.domain.dto.member.SteamLoginRequest;
import com.server.gummymurderer.domain.dto.member.SteamLoginResponse;
import com.server.gummymurderer.exception.Response;
import com.server.gummymurderer.service.SteamLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/steam")
@RequiredArgsConstructor
public class SteamLoginController {

    private final SteamLoginService steamLoginService;

    @PostMapping("/verify")
    public Response<SteamLoginResponse> verifySteamLogin(@RequestBody SteamLoginRequest request) throws JsonProcessingException {

        SteamLoginResponse response = steamLoginService.verifyAuthTicket(request);

        return Response.success(response);

    }
}