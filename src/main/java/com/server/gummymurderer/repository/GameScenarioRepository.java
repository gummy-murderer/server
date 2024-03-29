package com.server.gummymurderer.repository;

import com.server.gummymurderer.domain.entity.GameScenario;
import com.server.gummymurderer.domain.entity.GameSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameScenarioRepository extends JpaRepository<GameScenario, Long> {
    Optional<GameScenario> findByGameSet_GameSetNo(Long gameSetNo);

    Optional<GameScenario> findTopByGameSetOrderByScenarioNoDesc(GameSet gameSet);

}

