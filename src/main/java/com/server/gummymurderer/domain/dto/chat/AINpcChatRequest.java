package com.server.gummymurderer.domain.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AINpcChatRequest {

    private Long gameNo;
    private String secretKey;
    private String sender;
    private Map<String, String> npcName1 = new HashMap<>();
    private Map<String, String> npcName2 = new HashMap<>();
    private int chatDay;
    private List<Map<String, Object>> previousChatContents;
    private String previousStory;
    private String state;

}
