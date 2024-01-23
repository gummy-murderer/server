package com.server.gummymurderer.repository;

import com.server.gummymurderer.domain.entity.GameNpc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameNpcRepository extends JpaRepository<GameNpc, Long> {
}
