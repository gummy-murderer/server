package com.server.gummymurderer.service;

import com.server.gummymurderer.domain.dto.chat.ChatDto;
import com.server.gummymurderer.domain.dto.chatroom.ChatRoomDto;
import com.server.gummymurderer.domain.entity.Chat;
import com.server.gummymurderer.domain.entity.ChatRoom;
import com.server.gummymurderer.repository.ChatRepository;
import com.server.gummymurderer.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;

    // 채팅방 생성
    public ChatRoomDto createChatRoom() {
        ChatRoom chatRoom = ChatRoom.createChatRoom();
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        return new ChatRoomDto(savedChatRoom.getNo());
    }

    // 채팅 보내기
    public void sendChat(ChatDto chatDto) {
        ChatRoom chatRoom = chatRoomRepository.findByNo(chatDto.getChatRoomNo())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));
        Chat chat = Chat.builder()
                .receiver(chatDto.getReceiver())
                .receiverType(chatDto.getReceiverType())
                .sender(chatDto.getSender())
                .senderType(chatDto.getSenderType())
                .chatContent(chatDto.getChatContent())
                .chatDate(LocalDateTime.now())
                .chatRoom(chatRoom)
                .build();
        chatRepository.save(chat);
    }

    // 채팅방의 채팅 리스트 가져오기
    public List<ChatDto> getChats(Long chatRoomNo) {
        ChatRoom chatRoom = chatRoomRepository.findByNo(chatRoomNo)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));
        List<Chat> chats = chatRoom.getChats();
        return chats.stream().map(ChatDto::new).collect(Collectors.toList());
    }
}
