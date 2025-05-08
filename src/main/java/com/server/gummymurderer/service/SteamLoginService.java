package com.server.gummymurderer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.gummymurderer.configuration.jwt.JwtProvider;
import com.server.gummymurderer.domain.dto.game.LoginGameSetDTO;
import com.server.gummymurderer.domain.dto.member.SteamLoginRequest;
import com.server.gummymurderer.domain.dto.member.SteamLoginResponse;
import com.server.gummymurderer.domain.entity.GameUserCustom;
import com.server.gummymurderer.domain.entity.Member;
import com.server.gummymurderer.repository.GameSetRepository;
import com.server.gummymurderer.repository.GameUserCustomRepository;
import com.server.gummymurderer.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SteamLoginService {

    @Value("${steam.api-key}")
    private String STEAM_WEB_API_KEY;

    @Value("${steam.app-id}")
    private String STEAM_APP_ID;

    private static final String STEAM_AUTH_URL = "https://partner.steam-api.com/ISteamUserAuth/AuthenticateUserTicket/v1/";
    private static final RestTemplate restTemplate = new RestTemplate();

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtProvider jwtProvider;

    private final MemberRepository memberRepository; // 유저 정보 확인
    private final GameSetRepository gameSetRepository;
    private final GameUserCustomRepository gameUserCustomRepository;

    public SteamLoginResponse verifyAuthTicket(SteamLoginRequest request) throws JsonProcessingException {

//        log.info("🔑user authTicket : {}", request.getAuthTicket());
//        log.info("🔑user steamId : {}", request.getSteamId());
//        log.info("🔑user nickname : {}", request.getNickname());

        String requestUrl = STEAM_AUTH_URL +
                "?key=" + STEAM_WEB_API_KEY +
                "&appid=" + STEAM_APP_ID +
                "&ticket=" + request.getAuthTicket();

        ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
        log.info("🌐 Steam Auth API Response: {}", response.getBody());

        // Jackson을 사용하여 JSON 파싱
        JsonNode jsonResponse = objectMapper.readTree(response.getBody());
        String validatedSteamId = jsonResponse.path("response").path("params").path("steamid").asText();

        if (!validatedSteamId.equals(request.getSteamId())) {
            throw new RuntimeException("Steam ID 불일치");
        }

        Optional<Member> optionalMember = memberRepository.findBySteamId(request.getSteamId());
        boolean isNewUser = optionalMember.isEmpty();

        Member member = optionalMember.orElseGet(() -> {
            Member newMember = new Member(request.getSteamId(), request.getNickname());
            return memberRepository.save(newMember);
        });
        String token = jwtProvider.createToken(member.getSteamId(), member.getRoles());

        List<LoginGameSetDTO> gameSetList = isNewUser
                ? new ArrayList<>()
                : gameSetRepository.findGameSetsByMember(member)
                .stream()
                .map(gameSet -> {
                    GameUserCustom custom = gameUserCustomRepository.findByGameSet(gameSet).orElse(null);
                    return new LoginGameSetDTO(gameSet, custom);
                })
                .toList();

        log.info("🐻 loginGameSetDTO: {}", gameSetList);

        return new SteamLoginResponse(token, gameSetList);
    }
}