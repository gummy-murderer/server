package com.server.gummymurderer.domain.dto.game;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GameEndingLetterResponse {

    private String language;
    private Answer answer;

    @Getter
    @Setter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Answer {
        private String greeting;
        private String content;
        private String closing;
    }

    // ✅ AI 응답(=this)에 language만 덧붙인 새 객체 반환
    public static GameEndingLetterResponse of(GameEndingLetterResponse aiResponse, String language) {
        return new GameEndingLetterResponse(language, aiResponse.getAnswer());
    }

}