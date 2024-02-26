package com.server.gummymurderer.repository;

import com.server.gummymurderer.domain.entity.GameUserCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameUserCustomRepository extends JpaRepository<GameUserCustom, Long> {
}
