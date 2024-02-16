package com.server.gummymurderer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.server.gummymurderer.domain.dto.scenario.*;
import com.server.gummymurderer.domain.entity.Member;
import com.server.gummymurderer.exception.Response;
import com.server.gummymurderer.service.CustomUserDetails;
import com.server.gummymurderer.service.ScenarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/scenario")
@RequiredArgsConstructor
@Slf4j
public class ScenarioController {

    private final ScenarioService scenarioService;

    @PostMapping("/save")
    public ResponseEntity<Response<MakeScenarioResponse>> scenarioMake(@RequestBody MakeScenarioRequest request, @AuthenticationPrincipal CustomUserDetails customUserDetails) throws JsonProcessingException {
        Member loginMember = customUserDetails.getMember();
        MakeScenarioResponse makeScenarioResponse = scenarioService.makeScenario(request, loginMember);

        return ResponseEntity.ok().body(Response.success(makeScenarioResponse));
    }

    @PostMapping("/intro")
    public Response<IntroAnswerDTO> introMake(@RequestBody IntroRequest request, @AuthenticationPrincipal CustomUserDetails customUserDetails) throws JsonProcessingException {

        Member loginMember = customUserDetails.getMember();

        IntroAnswerDTO introAnswerDTO = scenarioService.intro(request, loginMember);
        return Response.success(introAnswerDTO);
    }

    @PostMapping("/final-words")
    public Response<FinalWordAnswerDTO> finalWordsMake(@RequestBody FinalWordRequest request, @AuthenticationPrincipal CustomUserDetails customUserDetails) throws JsonProcessingException {

        Member loginMember = customUserDetails.getMember();

        FinalWordAnswerDTO finalWordAnswerDTO = scenarioService.finalWords(request, loginMember);
        return Response.success(finalWordAnswerDTO);
    }
}
