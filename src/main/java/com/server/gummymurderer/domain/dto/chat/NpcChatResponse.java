package com.server.gummymurderer.domain.dto.chat;

import com.server.gummymurderer.domain.entity.Chat;
import com.server.gummymurderer.domain.enum_class.ChatRoleType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NpcChatResponse {

    private String sender;
    private String receiver;
    private String chatContent;
    private int chatDay;

    public static Chat toEntity(NpcChatResponse response, LocalDateTime time, ChatRoleType senderType, ChatRoleType receiverType) {
        return Chat.builder()
                .sender(response.sender)
                .receiver(response.receiver)
                .chatContent(response.chatContent)
                .chatDay(response.chatDay)
                .chatDate(time)
                .senderType(senderType)
                .receiverType(receiverType)
                .build();
    }

}
