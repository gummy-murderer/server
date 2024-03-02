package com.server.gummymurderer.controller;

import com.server.gummymurderer.domain.dto.member.LoginRequest;
import com.server.gummymurderer.domain.dto.member.SignRequest;
import com.server.gummymurderer.domain.dto.member.SignResponse;
import com.server.gummymurderer.exception.Response;
import com.server.gummymurderer.service.SignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
}
