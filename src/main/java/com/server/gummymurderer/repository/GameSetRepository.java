package com.server.gummymurderer.repository;

import com.server.gummymurderer.domain.entity.GameSet;
import com.server.gummymurderer.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GameSetRepository extends JpaRepository<GameSet, Long> {

    Optional<GameSet> findByGameSetNo(Long gameSetNo);

    Optional<GameSet> findByGameSetNoAndMember(Long gameSetNo, Member member);

    @Query("SELECT gs FROM GameSet gs JOIN FETCH gs.member m WHERE m = :member AND gs.gameStatus <> 'GAME_END'")
    List<GameSet> findGameSetsByMember(@Param("member") Member member);

    @Query("SELECT gs FROM GameSet gs JOIN FETCH gs.member m WHERE m = :member AND gs.gameSetNo = :gameSetNo AND gs.gameStatus = 'GAME_END'")
    Optional<GameSet> findEndedGameSetByMemberAndGameSetNo(@Param("gameSetNo") Long gameSetNo, @Param("member") Member member);

}
