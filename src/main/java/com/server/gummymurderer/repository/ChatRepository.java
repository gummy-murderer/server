package com.server.gummymurderer.repository;

import com.server.gummymurderer.domain.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("SELECT c FROM Chat c where ((c.sender = :userName and c.receiver = :aiNpcName) or (c.sender = :aiNpcName and c.receiver = :userName)) and c.gameSet.gameSetNo = :gameSetNo")
    //@Query에서 콜론(:) 뒤에 붙은 문자열은 해당 메소드의 파라미터를 참조
    List<Chat> findAllByUserAndAINpcAndGameSetNo(String userName, String aiNpcName, Long gameSetNo);

}
