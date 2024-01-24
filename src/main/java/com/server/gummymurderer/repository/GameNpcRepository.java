package com.server.gummymurderer.repository;

import com.server.gummymurderer.domain.entity.GameNpc;
import com.server.gummymurderer.domain.entity.GameSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface GameNpcRepository extends JpaRepository<GameNpc, Long> {

    Optional<GameNpc> findByGameNpcNo(Long gameNpcNo);

    Optional<GameNpc> findByNpcName(String npcName);
    List<GameNpc> findAllByGameSet (GameSet gameSet);
}
