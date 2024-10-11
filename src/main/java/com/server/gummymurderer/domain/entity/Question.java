package com.server.gummymurderer.domain.entity;

import com.server.gummymurderer.domain.enum_class.KeyWordType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "question_tb")
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_no")
    private Long questionNo;

    private String npcName;

    private String keyWord;

    @Enumerated(EnumType.STRING)
    private KeyWordType keyWordType;

    @Column(name = "question_index")
    private Integer questionIndex;

    @Column(name = "question_text")
    private String questionText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_set_no")
    private GameSet gameSet;

}
