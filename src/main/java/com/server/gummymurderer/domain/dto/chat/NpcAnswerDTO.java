package com.server.gummymurderer.domain.dto.chat;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NpcAnswerDTO {

    private List<ChatContent> chatContent;

}
