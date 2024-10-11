package com.server.gummymurderer.domain.dto.question;

import com.server.gummymurderer.domain.entity.GameSet;
import com.server.gummymurderer.domain.entity.Question;
import com.server.gummymurderer.domain.enum_class.KeyWordType;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCreateRequest {

    private Long gameSetNo;
    private String npcName;
    private String keyWord;
    private String keyWordType;

    public static Question toEntity(QuestionCreateRequest request, KeyWordType keyWordType, GameSet gameSet) {
        return Question.builder()
                .npcName(request.npcName)
                .keyWord(request.keyWord)
                .keyWordType(keyWordType)
                .gameSet(gameSet)
                .build();
    }
}
