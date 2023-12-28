package com.server.gummymurderer.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatRoom extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @OneToMany(mappedBy = "chatRoom")
    private List<Chat> chats = new ArrayList<>();

    public static ChatRoom createChatRoom() {
        return new ChatRoom();
    }
}
