package com.server.gummymurderer.repository;

import com.server.gummymurderer.domain.entity.GameSet;
import com.server.gummymurderer.domain.entity.Member;
import com.server.gummymurderer.domain.entity.Npc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameSetRepository extends JpaRepository<GameSet, Long> {

    Optional<GameSet> findByGameSetNo(Long gameSetNo);
    Optional<GameSet> findByGameSetNoAndMember(Long gameSetNo, Member member);

}
