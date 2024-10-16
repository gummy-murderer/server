package com.server.gummymurderer.repository;

import com.server.gummymurderer.domain.dto.scenario.LivingCharacters;
import com.server.gummymurderer.domain.dto.scenario.NpcInfo;
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

    @Query("SELECT new com.server.gummymurderer.domain.dto.scenario.NpcInfo(n.npcName, n.gameNpcNo) FROM GameNpc n WHERE n.gameSet.gameSetNo = :gameSetNo AND n.npcJob = 'Resident' AND (n.npcStatus = 'alive' OR n.npcStatus = 'ALIVE')")
    List<NpcInfo> findAllAliveResidentNpcInfoByGameSetNo(@Param("gameSetNo") Long gameSetNo);

    @Query("SELECT new com.server.gummymurderer.domain.dto.scenario.LivingCharacters(n.npcName, n.npcJob, n.npcStatus) FROM GameNpc n WHERE n.gameSet.gameSetNo = :gameSetNo")
    List<LivingCharacters> findAllLivingCharactersByGameSetNo(@Param("gameSetNo") Long gameSetNo);

    @Query(value = "SELECT npc_name FROM gummymurderer.game_npc_tb WHERE game_set_no = :gameSetNo AND npc_job = 'Murderer'", nativeQuery = true)
    String findMurderByGameSetNo(@Param("gameSetNo") Long gameSetNo);

    Optional<GameNpc> findByNpcNameAndGameSet(String npcName, GameSet gameSet);

    Optional<GameNpc> findByNpcNameAndGameSet_GameSetNo(String npcName, Long gameSetNo);

    List<GameNpc> findAllByNpcNameInAndGameSet_GameSetNo(List<String> npcNames, Long gameSetNo);

    Optional<GameNpc> findByGameNpcNoAndGameSet(Long gameNpcNo, GameSet gameSet);
}
