package com.server.gummymurderer.repository;

import com.server.gummymurderer.domain.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c FROM Chat c where ((c.sender = :nickName and c.receiver = :aiNpcName) or (c.sender = :aiNpcName and c.receiver = :nickName)) and c.gameSet.gameSetNo = :gameSetNo")
        //@Query에서 콜론(:) 뒤에 붙은 문자열은 해당 메소드의 파라미터를 참조
    List<Chat> findAllByMemberAndAINpcAndGameSetNo(String nickName, String aiNpcName, Long gameSetNo);

    @Query("SELECT c FROM Chat c where ((c.sender = :npcName1 and c.receiver = :npcName2) or (c.sender = :npcName2 and c.receiver = :npcName1)) and c.gameSet.gameSetNo = :gameSetNo")
    List<Chat> findAllByNpcAndGameSetNo(String npcName1, String npcName2, Long gameSetNo);


}
