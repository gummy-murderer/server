package com.server.gummymurderer.controller;

import com.server.gummymurderer.domain.dto.chat.*;
import com.server.gummymurderer.domain.entity.Member;
import com.server.gummymurderer.exception.Response;
import com.server.gummymurderer.service.ChatService;
import com.server.gummymurderer.service.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;

    // 채팅 보내기 unity 테스트 용
//    @PostMapping("/send")
//    public Mono<Response<ChatSaveResponse>> sendChat(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ChatSaveRequest request, HttpServletRequest httpServletRequest) {
//        String contentType = httpServletRequest.getHeader("Content-Type");
//        System.out.println("Content-Type: " + contentType);
//
//        String authorizationHeader = httpServletRequest.getHeader("Authorization");
//        log.info("🐻Authorization header: {}", authorizationHeader); // 토큰 출력
//
//        Member loginMember = userDetails.getMember();
//
//        // 요청 정보 로그
//        log.info("🐻Request URL: {}", httpServletRequest.getRequestURL());
//        log.info("🐻Request Method: {}", httpServletRequest.getMethod());
//        log.info("🐻Request Body: {}", request.toString());
//
//        return chatService.saveChatTest(loginMember, request)
//                .map(Response::success);
//    }

    // user-npc 채팅
    @PostMapping("/send")
    public Response<ChatSaveResponse> sendChat(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ChatSaveRequest request, HttpServletRequest httpServletRequest) {
        String contentType = httpServletRequest.getHeader("Content-Type");
        System.out.println("Content-Type: " + contentType);

        Member loginMember = userDetails.getMember();

        log.info("🐻user-npc 통신 완료");

        ChatSaveResponse chatSaveResponse = chatService.saveChat(loginMember, request, httpServletRequest);
        return Response.success(chatSaveResponse);
    }

    // npc-npc 채팅
    @PostMapping("/npc")
    public Response<ChatContent> npcChat(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody NpcChatRequest npcChatRequest) {

        Member loginMember = userDetails.getMember();

        ChatContent chatContent = chatService.getNpcChat(loginMember, npcChatRequest);
        return Response.success(chatContent);
    }

    // aiNpc 별 채팅 조회
    @GetMapping("/list")
    public Response<List<ChatListResponse>> getAllChatByUserAndAINpc(@AuthenticationPrincipal CustomUserDetails userDetails, @ModelAttribute ChatListRequest chatListRequest, HttpServletRequest httpServletRequest) {

        String requestURL = httpServletRequest.getRequestURI();
        String queryString = httpServletRequest.getQueryString();

        log.info("🐻Request URL: {}", requestURL);
        log.info("🐻Query String: {}", queryString);

        Member loginMember = userDetails.getMember();

        List<ChatListResponse> chats = chatService.getAllChatByUserNameAndAINpc(loginMember, chatListRequest);
        return Response.success(chats);
    }


}