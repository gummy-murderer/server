package com.server.gummymurderer.domain.entity;

import com.server.gummymurderer.domain.enum_class.InterrogationStatus;
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

    @Lob
    @Column(columnDefinition = "TEXT")
    private String answer;

    private Integer heartRate;
    private boolean isMurderer;

    @Enumerated(EnumType.STRING)
    private InterrogationStatus interrogationStatus;

    public static InterrogationDialogue fromRequest(String userQuestion, String answer, int heartRate, Interrogation interrogation, boolean isMurderer, InterrogationStatus interrogationStatus) {
        InterrogationDialogue dialogue = new InterrogationDialogue();
        dialogue.userQuestion = userQuestion;
        dialogue.answer = answer;
        dialogue.heartRate = heartRate;
        dialogue.interrogation = interrogation;
        dialogue.isMurderer = isMurderer;
        dialogue.interrogationStatus = interrogationStatus;
        return dialogue;
    }
}
