package com.server.gummymurderer.repository;

import com.server.gummymurderer.domain.entity.GameSet;
import com.server.gummymurderer.domain.entity.InterrogationDialogue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterrogationDialogueRepository extends JpaRepository<InterrogationDialogue, Long> {

    void deleteByInterrogation_GameSet(GameSet gameSet);

}
