package com.server.gummymurderer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.server.gummymurderer.domain.dto.game.*;
import com.server.gummymurderer.domain.dto.gameNpc.GameNpcInfoRequest;
import com.server.gummymurderer.domain.dto.gameNpc.GameNpcInfoResponse;
import com.server.gummymurderer.domain.entity.Member;
import com.server.gummymurderer.exception.Response;
import com.server.gummymurderer.service.CustomUserDetails;
import com.server.gummymurderer.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/game")
@RequiredArgsConstructor
@Slf4j
public class GameController {

    private final GameService gameService;

    @PostMapping("/key")
    public Response<SecretKeyValidationResponse> validateSecretKey(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody SecretKeyValidationRequest request) throws JsonProcessingException {

        Member loginMember = customUserDetails.getMember();
        SecretKeyValidationResponse response = gameService.validationSecretKey(loginMember, request);

        HttpStatus statusCode = response.isValid() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        log.info("🐻 Response status code: {}", statusCode);

        return Response.success(response);
    }

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

    @GetMapping("/load")
    public Response<LoadGameResponse> loadGame(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam Long gameSetNo) {

        Member loginMember = customUserDetails.getMember();
        LoadGameResponse loadGameResponse = gameService.gameLoad(loginMember, gameSetNo);

        return Response.success(loadGameResponse);
    }

    @PostMapping("/end")
    public ResponseEntity<Response<EndGameResponse>> endGame(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody EndGameRequest request) {
        Member loginMember = customUserDetails.getMember();
        EndGameResponse endGameResponse = gameService.gameEnd(loginMember, request);
        return ResponseEntity.ok(Response.success(endGameResponse));
    }

    @GetMapping("/npc/info")
    public Response<GameNpcInfoResponse> getNpcInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails, @ModelAttribute GameNpcInfoRequest request){

        Member loginMember = customUserDetails.getMember();
        GameNpcInfoResponse gameNpcInfoResponse = gameService.gameNpcInfo(loginMember, request);

        return  Response.success(gameNpcInfoResponse);

    }
}
