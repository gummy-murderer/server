package com.server.gummymurderer.repository;


import com.server.gummymurderer.domain.entity.Npc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NpcRepository extends JpaRepository<Npc, Long> {

}
