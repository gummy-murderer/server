package com.server.gummymurderer.repository;

import com.server.gummymurderer.domain.entity.GameNpc;
import com.server.gummymurderer.domain.entity.GameSet;
import com.server.gummymurderer.domain.entity.GameUserDetectiveNotebook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameUserDetectiveNotebookRepository extends JpaRepository<GameUserDetectiveNotebook, Long> {

    List<GameUserDetectiveNotebook> findByGameNpc_GameSet(GameSet gameSet);

    Optional<GameUserDetectiveNotebook> findByGameNpcAndGameSet(GameNpc gameNpc, GameSet gameSet);

}
