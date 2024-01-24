package com.server.gummymurderer.repository;

import com.server.gummymurderer.domain.entity.GameUserCheckList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameUserCheckListRepository extends JpaRepository<GameUserCheckList, Long> {
}
