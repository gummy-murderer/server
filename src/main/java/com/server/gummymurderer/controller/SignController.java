package com.server.gummymurderer.controller;

import com.server.gummymurderer.domain.dto.member.*;
import com.server.gummymurderer.exception.Response;
import com.server.gummymurderer.service.SignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class SignController {

    private final SignService memberService;

    @PostMapping(value = "/sign-in")
    public Response<SignResponse> signIn(@RequestBody LoginRequest request) throws Exception {
        SignResponse signResponse = memberService.login(request);
        return Response.success(signResponse);
    }

    @PostMapping(value = "/register")
    public Response<SignResponse> register(@RequestBody @Valid SignRequest request) throws Exception {
        SignResponse signResponse = memberService.register(request);
        return Response.success(signResponse);
    }

    @PostMapping(value = "/check-account")
    public Response<DuplicatedResponse> checkAccount(@RequestBody DuplicatedAccountRequest request) {
        DuplicatedResponse response = memberService.duplicateCheckAccount(request);
        log.info("🐻response : {}", response);
        return Response.success(response);
    }

    @PostMapping(value = "/check-email")
    public Response<DuplicatedResponse> checkEmail(@RequestBody DuplicatedEmailRequest request) {
        DuplicatedResponse response = memberService.duplicateCheckEmail(request);
        return Response.success(response);
    }

    @PostMapping(value = "/check-nickname")
    public Response<DuplicatedResponse> checkNickname(@RequestBody DuplicatedNicknameRequest request) {
        DuplicatedResponse response = memberService.duplicateCheckNickname(request);
        return Response.success(response);
    }
}
