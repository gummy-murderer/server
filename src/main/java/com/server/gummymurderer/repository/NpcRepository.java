package com.server.gummymurderer.repository;


import com.server.gummymurderer.domain.entity.Npc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NpcRepository extends JpaRepository<Npc, Long> {

    Optional<Npc> findByNpcNo(long npcNo);
}
