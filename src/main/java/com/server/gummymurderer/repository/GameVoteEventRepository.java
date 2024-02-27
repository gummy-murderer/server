package com.server.gummymurderer.repository;

import com.server.gummymurderer.domain.entity.GameSet;
import com.server.gummymurderer.domain.entity.GameVoteEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameVoteEventRepository extends JpaRepository<GameVoteEvent, Long> {

    Long countAllByGameSet(GameSet gameSet);
}
