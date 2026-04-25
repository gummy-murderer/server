package com.server.gummymurderer.repository;

import com.server.gummymurderer.domain.entity.GameNpc;
import com.server.gummymurderer.domain.entity.GameNpcCustom;
import com.server.gummymurderer.domain.entity.GameSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameNpcCustomRepository extends JpaRepository<GameNpcCustom, Long> {

    Optional<GameNpcCustom> findByGameNpc(GameNpc gameNpc);

    void deleteByGameSet(GameSet gameSet);
}
