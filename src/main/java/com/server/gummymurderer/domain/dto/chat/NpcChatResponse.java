package com.server.gummymurderer.domain.dto.chat;

import com.server.gummymurderer.domain.dto.scenario.TokensDTO;
import com.server.gummymurderer.domain.entity.Chat;
import com.server.gummymurderer.domain.entity.GameSet;
import com.server.gummymurderer.domain.enum_class.ChatRoleType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NpcChatResponse {

    private NpcAnswerDTO answer;
    private TokensDTO tokens;

    public static Chat toEntity(ChatContent chatContent, int chatDay, LocalDateTime time, ChatRoleType senderType, ChatRoleType receiverType, GameSet gameSet) {
        // chatContent를 추출하고 Chat 객체로 변환
        return ChatContent.toEntity(chatContent, chatDay, time, senderType, receiverType, gameSet);
    }

}
