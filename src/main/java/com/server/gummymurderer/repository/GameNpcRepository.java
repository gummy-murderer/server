package com.server.gummymurderer.repository;

import com.server.gummymurderer.domain.entity.GameNpc;
import com.server.gummymurderer.domain.entity.GameSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GameNpcRepository extends JpaRepository<GameNpc, Long> {

    Optional<GameNpc> findByGameNpcNo(Long gameNpcNo);

    Optional<GameNpc> findByNpcName(String npcName);

    List<GameNpc> findAllByGameSet(GameSet gameSet);

    @Query(value = "SELECT npc_name FROM gummymurderer.game_npc_tb WHERE game_set_no = :gameSetNo AND npc_job = 'Resident' AND npc_status = 'alive'", nativeQuery = true)
    List<String> findAllAliveResidentNpcNamesByGameSetNo(@Param("gameSetNo") Long gameSetNo);

    @Query(value = "SELECT npc_name FROM gummymurderer.game_npc_tb WHERE game_set_no = :gameSetNo AND npc_job = 'Murderer'", nativeQuery = true)
    String findMurderByGameSetNo(@Param("gameSetNo") Long gameSetNo);

    Optional<GameNpc> findByNpcNameAndGameSet(String npcName, GameSet gameSet);

    Optional<GameNpc> findByNpcNameAndGameSet_GameSetNo(String npcName, Long gameSetNo);

    List<GameNpc> findAllByNpcNameInAndGameSet_GameSetNo(List<String> npcNames, Long gameSetNo);

    Optional<GameNpc> findByGameNpcNoAndGameSet(Long gameNpcNo, GameSet gameSet);
}
