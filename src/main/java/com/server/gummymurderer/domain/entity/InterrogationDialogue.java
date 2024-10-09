package com.server.gummymurderer.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "interrogation_dialogue_tb")
public class InterrogationDialogue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interrogationDialogueNo;

    @ManyToOne
    @JoinColumn(name = "interrogation_no")
    private Interrogation interrogation;

    private String userQuestion;
    private String answer;
    private Integer heartRate;

    public static InterrogationDialogue fromRequest(String userQuestion, String answer, int heartRate, Interrogation interrogation) {
        InterrogationDialogue dialogue = new InterrogationDialogue();
        dialogue.userQuestion = userQuestion;
        dialogue.answer = answer;
        dialogue.heartRate = heartRate;
        dialogue.interrogation = interrogation;
        return dialogue;
    }
}
