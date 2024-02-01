package com.server.gummymurderer.controller;

import com.server.gummymurderer.domain.dto.game.*;
import com.server.gummymurderer.domain.entity.Member;
import com.server.gummymurderer.exception.Response;
import com.server.gummymurderer.service.CustomUserDetails;
import com.server.gummymurderer.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/game")
@RequiredArgsConstructor
@Slf4j
public class GameController {

    private final GameService gameService;

    @PostMapping("/start")
    public ResponseEntity<Response<StartGameResponse>> startGame(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Member loginMember = customUserDetails.getMember();
        StartGameResponse response = gameService.startGame(loginMember);
        return ResponseEntity.ok().body(Response.success(response));
    }

    @PostMapping("/save")
    public ResponseEntity<Response<SaveGameResponse>> saveGame(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody SaveGameRequest request) {
        Member loginMember = customUserDetails.getMember();
        SaveGameResponse response = gameService.gameSave(loginMember, request);
        return ResponseEntity.ok(Response.success(response));
    }

    @PostMapping("/end")
    public ResponseEntity<Response<EndGameResponse>> endGame(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody EndGameRequest request) {
        Member loginMember = customUserDetails.getMember();
        EndGameResponse endGameResponse = gameService.gameEnd(loginMember, request);
        return ResponseEntity.ok(Response.success(endGameResponse));
    }
}
