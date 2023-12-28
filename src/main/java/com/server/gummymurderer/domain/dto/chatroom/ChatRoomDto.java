package com.server.gummymurderer.domain.dto.chatroom;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomDto {

    private Long no;
    private String lastModifiedAt;

    public ChatRoomDto(Long no) {
        this.no = no;
    }
}
