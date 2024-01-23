package com.server.gummymurderer.repository;

import com.server.gummymurderer.domain.entity.GameNpc;
import com.server.gummymurderer.domain.entity.GameSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameNpcRepository extends JpaRepository<GameNpc, Long> {

    Optional<GameNpc> findByNpcName(String npcName);
    List<GameNpc> findAllByGameSet (GameSet gameSet);
}
