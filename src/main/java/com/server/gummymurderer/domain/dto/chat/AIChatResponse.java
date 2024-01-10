package com.server.gummymurderer.domain.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AIChatResponse {

    private String sender;       // 수신자 이름
    private String receiver;     // 발신자 이름
    private String chatContent;  // 채팅 내용

}

