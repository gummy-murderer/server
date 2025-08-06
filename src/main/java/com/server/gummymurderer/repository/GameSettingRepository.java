package com.server.gummymurderer.repository;

import com.server.gummymurderer.domain.entity.GameSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameSettingRepository extends JpaRepository<GameSetting, Long> {

    @Query("SELECT gs FROM GameSetting gs WHERE gs.member.memberNo = :memberNo")
    Optional<GameSetting> findByMemberNo(@Param("memberNo") Long memberNo);


}
