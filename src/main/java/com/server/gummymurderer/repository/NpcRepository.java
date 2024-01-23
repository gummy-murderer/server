package com.server.gummymurderer.repository;


import com.server.gummymurderer.domain.entity.Npc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface NpcRepository extends JpaRepository<Npc, Long> {

    Optional<Npc> findByNpcNo(long npcNo);
    Optional<Npc> findByNpcName(String npcName);
    Page<Npc> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM npc_tb order by RAND() limit 7", nativeQuery = true)
    List<Npc> findRandom7Npc();}
