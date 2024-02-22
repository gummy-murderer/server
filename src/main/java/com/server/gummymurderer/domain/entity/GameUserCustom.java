package com.server.gummymurderer.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "game_user_custom_tb")
public class GameUserCustom extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_custom_no")
    private Long userCustomNo;

    @Column(name = "head")
    private String head;

    @Column(name = "eye")
    private String eye;

    @Column(name = "mouth")
    private String mouth;

    @Column(name = "ear")
    private String ear;

    @Column(name = "body")
    private String body;

    @Column(name = "tail")
    private String tail;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_set_no")
    private GameSet gameSet;

}
