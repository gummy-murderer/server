package com.server.gummymurderer.domain.dto.chat;

import com.server.gummymurderer.domain.entity.Chat;
import com.server.gummymurderer.domain.entity.GameSet;
import com.server.gummymurderer.domain.enum_class.ChatRoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatContent {

    private String sender;
    private String receiver;
    private String chatContent;

    public static Chat toEntity(ChatContent chatContent, int chatDay, LocalDateTime time, ChatRoleType senderType, ChatRoleType receiverType, GameSet gameSet) {
        return Chat.builder()
                .sender(chatContent.sender)
                .receiver(chatContent.receiver)
                .chatContent(chatContent.chatContent)
                .chatDay(chatDay)
                .chatDate(time)
                .senderType(senderType)
                .receiverType(receiverType)
                .gameSet(gameSet)
                .build();
    }
}
