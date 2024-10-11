package com.server.gummymurderer.domain.entity;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_no")
    private Question question;

    @Column(name = "answer_text")
    private String answerText;

}
