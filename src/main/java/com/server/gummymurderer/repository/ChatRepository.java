package com.server.gummymurderer.repository;

import com.server.gummymurderer.domain.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    Optional<Chat> findByNo(Long no);

}
