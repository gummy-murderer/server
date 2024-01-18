package com.server.gummymurderer.domain.dto.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NpcChatRequestDto {
    private String sender;
    private String npcName1;
    private String npcName2;
    private int chatDay;
}
