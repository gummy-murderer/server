package com.server.gummymurderer.domain.entity;

import com.server.gummymurderer.domain.enum_class.KeyWordType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "question_answer_tb")
public class QuestionAnswer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_answer_no")
    private Long questionAnswerNo;

    @Column(name = "game_set_no")
    private Long gameSetNo;

    @Column(name = "npc_name")
    private String npcName;

    @Column(name = "keyword")
    private String keyWord;

    @Enumerated(EnumType.STRING)
    private KeyWordType keyWordType;

    @Column(name = "answer_text")
    private String answerText;

}
