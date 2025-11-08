package com.server.gummymurderer.domain.dto.game;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Answer {

    private String victim;
    private String crimeScene;
    private String method;

}
