package com.server.gummymurderer.domain.dto.chat;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NpcChatRequest {

    private String sender;
    private String npcName1;
    private String npcName2;
    private String previousStory;

}
