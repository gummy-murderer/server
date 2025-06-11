package com.server.gummymurderer.repository;

import com.server.gummymurderer.domain.entity.GameSetting;
import com.server.gummymurderer.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameSettingRepository extends JpaRepository<GameSetting, Long> {

    Optional<GameSetting> findByMember(Member member);

}
