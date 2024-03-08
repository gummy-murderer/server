package com.server.gummymurderer.domain.dto.chat;

import com.server.gummymurderer.domain.dto.scenario.TokensDTO;
import com.server.gummymurderer.domain.entity.Chat;
import com.server.gummymurderer.domain.entity.GameSet;
import com.server.gummymurderer.domain.enum_class.ChatRoleType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NpcChatResponse {

    private ChatContent answer;
    private TokensDTO tokens;

    public Chat toEntity(int chatDay, LocalDateTime time, ChatRoleType senderType, ChatRoleType receiverType, GameSet gameSet) {
        return ChatContent.toEntity(answer, chatDay, time, senderType, receiverType, gameSet);
    }
}
