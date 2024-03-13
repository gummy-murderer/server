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
    public Response<String> checkAccount(@RequestBody DuplicatedAccountRequest request) {
        String message = memberService.duplicateCheckAccount(request);
        log.info(message);
        return Response.success(message);
    }

    @PostMapping(value = "/check-email")
    public Response<String> checkEmail(@RequestBody DuplicatedEmailRequest request) {
        String message = memberService.duplicateCheckEmail(request);
        return Response.success(message);
    }

    @PostMapping(value = "/check-nickname")
    public Response<String> checkNickname(@RequestBody DuplicatedNicknameRequest request) {
        String message = memberService.duplicateCheckNickname(request);
        return Response.success(message);
    }
}
