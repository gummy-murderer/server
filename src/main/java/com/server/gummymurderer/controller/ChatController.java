package com.server.gummymurderer.controller;

import com.server.gummymurderer.domain.dto.chat.ChatDto;
import com.server.gummymurderer.domain.dto.chatroom.ChatRoomDto;
import com.server.gummymurderer.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    // 채팅방 생성
    @PostMapping("/room")
    public ResponseEntity<?> createChatRoom() {
        ChatRoomDto chatRoom = chatService.createChatRoom();
        return new ResponseEntity<>(chatRoom, HttpStatus.CREATED);
    }

    // 채팅 보내기
    @PostMapping("/send")
    public ResponseEntity<?> sendChat(@RequestBody ChatDto chatDto) {
        chatService.sendChat(chatDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 채팅방의 채팅 리스트 가져오기
    @GetMapping("/room/{roomId}")
    public ResponseEntity<?> getChats(@PathVariable Long roomId) {
        List<ChatDto> chatList = chatService.getChats(roomId);
        return new ResponseEntity<>(chatList, HttpStatus.OK);
    }
}
