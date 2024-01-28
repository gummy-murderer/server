package com.server.gummymurderer.controller;

import com.server.gummymurderer.domain.dto.chat.*;
import com.server.gummymurderer.exception.Response;
import com.server.gummymurderer.service.ChatService;
import com.server.gummymurderer.service.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // 채팅 보내기

    @PostMapping("/send")
    public Mono<Response<ChatSaveResponse>> sendChat(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ChatSaveRequest request, HttpServletRequest httpServletRequest) {
        String contentType = httpServletRequest.getHeader("Content-Type");
        System.out.println("Content-Type: " + contentType);

        return chatService.saveChat(userDetails, request)
                .map(Response::success);
    }

    // aiNpc 별 채팅 조회
    @GetMapping("/list")
    public Response<List<ChatListResponse>> getAllChatByUserAndAINpc(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam ChatListRequest chatListRequest) {
        List<ChatListResponse> chats = chatService.getAllChatByUserNameAndAINpc(userDetails, chatListRequest);
        return Response.success(chats);
    }

    @PostMapping("/npc")
    public Mono<Response<List<NpcChatResponse>>> npcChat(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody NpcChatRequestDto npcChatRequestDto) {
        Mono<List<NpcChatResponse>> npcChatResponseList = chatService.getNpcChat(userDetails, npcChatRequestDto);

        return npcChatResponseList
                .map(Response::success);
    }
}
