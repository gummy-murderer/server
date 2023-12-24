package com.server.gummymurderer.domain.entity;

import com.server.gummymurderer.domain.enum_class.ChatType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Chat{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    private String receiver;

    @Enumerated(EnumType.STRING)
    private ChatType receiverType;

    private String sender;

    @Enumerated(EnumType.STRING)
    private ChatType senderType;

    private String chatContent;

    @Column(name = "chat_date")
    private LocalDateTime chatDate;

    @Column(name = "chat_day")
    private int chatDay;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "game_no")
//    private Game game;

}
