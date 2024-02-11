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
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final ChatService chatService;

    // 채팅 보내기 unity 테스트 용
    @PostMapping("/send")
    public Mono<Response<ChatSaveResponse>> sendChat(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ChatSaveRequest request, HttpServletRequest httpServletRequest) {
        String contentType = httpServletRequest.getHeader("Content-Type");
        System.out.println("Content-Type: " + contentType);

        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        log.info("🐻Authorization header: {}", authorizationHeader); // 토큰 출력

        // 요청 정보 로그
        log.info("🐻Request URL: {}", httpServletRequest.getRequestURL());
        log.info("🐻Request Method: {}", httpServletRequest.getMethod());
        log.info("🐻Request Body: {}", request.toString());

        return chatService.saveChatTest(userDetails, request)
                .map(Response::success);
    }

    // 채팅 보내기
//    @PostMapping("/send")
//    public Mono<Response<ChatSaveResponse>> sendChat(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ChatSaveRequest request, HttpServletRequest httpServletRequest) {
//        String contentType = httpServletRequest.getHeader("Content-Type");
//        System.out.println("Content-Type: " + contentType);
//
//        return chatService.saveChat(userDetails, request)
//                .map(Response::success);
//    }

    // aiNpc 별 채팅 조회
    @GetMapping("/list")
    public Response<List<ChatListResponse>> getAllChatByUserAndAINpc(@ModelAttribute ChatListRequest chatListRequest) {
        List<ChatListResponse> chats = chatService.getAllChatByUserNameAndAINpc(chatListRequest);
        return Response.success(chats);
    }

    @PostMapping("/npc")
    public Mono<Response<NpcChatResponse>> npcChat(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody NpcChatRequestDto npcChatRequestDto) {
        Mono<NpcChatResponse> npcChatResponseList = chatService.getNpcChat(userDetails, npcChatRequestDto);

        return npcChatResponseList
                .map(Response::success);
    }
}