package com.server.gummymurderer.domain.dto.chat;

import com.server.gummymurderer.domain.entity.Chat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatListResponse {

    private String sender;
    private String receiver;
    private String chatContent;

    public static ChatListResponse of(Chat chat) {
        return new ChatListResponse(chat.getSender(), chat.getReceiver(), chat.getChatContent());
    }
}
