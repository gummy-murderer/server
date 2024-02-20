package com.server.gummymurderer.repository;

import com.server.gummymurderer.domain.entity.GameSet;
import com.server.gummymurderer.domain.entity.GameUserCheckList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameUserCheckListRepository extends JpaRepository<GameUserCheckList, Long> {

    List<GameUserCheckList> findByGameNpc_GameSet(GameSet gameSet);

}
