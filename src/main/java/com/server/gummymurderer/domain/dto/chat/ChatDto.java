package com.server.gummymurderer.domain.dto.chat;

import com.server.gummymurderer.domain.entity.Chat;
import com.server.gummymurderer.domain.enum_class.ChatRoleType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatDto {

    private Long no;
    private String receiver;
    @Enumerated(EnumType.STRING)
    private ChatRoleType receiverType;
    private String sender;
    @Enumerated(EnumType.STRING)
    private ChatRoleType senderType;
    private String chatContent;
    private LocalDateTime chatDate;
    private int chatDay;
    private Long chatRoomNo;

    public ChatDto(Chat chat) {
        this.chatRoomNo = chat.getChatRoom().getNo();
        this.receiver = chat.getReceiver();
        this.receiverType = chat.getReceiverType();
        this.sender = chat.getSender();
        this.senderType = chat.getSenderType();
        this.chatContent = chat.getChatContent();
    }
}
