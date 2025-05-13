package com.server.gummymurderer.repository;

import com.server.gummymurderer.domain.entity.GameSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameSettingRepository extends JpaRepository<GameSetting, Long> {


}
