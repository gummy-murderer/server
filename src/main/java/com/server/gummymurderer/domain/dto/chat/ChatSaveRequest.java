package com.server.gummymurderer.domain.dto.chat;

import com.server.gummymurderer.domain.entity.Chat;
import com.server.gummymurderer.domain.entity.GameSet;
import com.server.gummymurderer.domain.enum_class.ChatRoleType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatSaveRequest {

    private String sender;
    private String receiver;
    private String chatContent;
    private int chatDay;
    private Long gameSetNo;
    private String secretKey;

    public static Chat toEntity(ChatSaveRequest request, LocalDateTime time, ChatRoleType senderType, ChatRoleType receiverType, GameSet gameSet) {
        return Chat.builder()
                .sender(request.sender)
                .receiver(request.receiver)
                .chatContent(request.chatContent)
                .chatDay(request.chatDay)
                .chatDate(time)
                .senderType(senderType)
                .receiverType(receiverType)
                .gameSet(gameSet)
                .build();

    }
}
