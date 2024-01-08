package com.server.gummymurderer.controller;

import com.server.gummymurderer.domain.dto.chat.ChatSaveRequest;
import com.server.gummymurderer.domain.dto.chat.ChatSaveResponse;
import com.server.gummymurderer.exception.Response;
import com.server.gummymurderer.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // 채팅 보내기
    @PostMapping("/send")
    public Mono<Response<ChatSaveResponse>> sendChat(@RequestBody ChatSaveRequest request) {
        return chatService.saveChat(request)
                .map(Response::success);
    }
}
