package com.server.gummymurderer.domain.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AIChatRequest {

    private String sender;
    private String receiver;
    private String chatContent;
    private int chatDay;
    private List<Map<String, Object>> previousChatContents; // 이전 대화 내용들
    private String npcName;
    private String npcPersonality;
    private String npcPersonalityDescription;
    private String npcFeature;
    private String npcFeatureDescription;

}
