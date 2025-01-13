package com.server.gummymurderer.domain.dto.question;

import com.server.gummymurderer.domain.entity.GameSet;
import com.server.gummymurderer.domain.entity.Question;
import com.server.gummymurderer.domain.enum_class.KeyWordType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCreateResponse {

    private String question;

    public static Question toEntity(QuestionCreateDTO dto, GameSet gameSet, QuestionCreateRequest request) {
        return Question.builder()
                .npcName(request.getNpcName())
                .keyWord(request.getKeyWord())
                .keyWordType(KeyWordType.valueOf(request.getKeyWordType().toUpperCase()))
                .gameSet(gameSet)
                .questionText(dto.getQuestion())
                .build();
    }

}
