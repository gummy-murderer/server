package com.server.gummymurderer.domain.dto.chat;

import com.server.gummymurderer.domain.entity.Chat;
import com.server.gummymurderer.domain.entity.ChatRoom;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatSaveRequest {

    private String sender;
    private String receiver;
    private String chatContent;
    private int chatDay;

    public static Chat toEntity(ChatSaveRequest request, LocalDateTime time) {
        return Chat.builder()
                .sender(request.sender)
                .receiver(request.receiver)
                .chatContent(request.chatContent)
                .chatDay(request.chatDay)
                .chatDate(time)
                .build();

    }


}
