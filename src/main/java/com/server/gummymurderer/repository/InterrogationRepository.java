package com.server.gummymurderer.repository;

import com.server.gummymurderer.domain.entity.GameSet;
import com.server.gummymurderer.domain.entity.Interrogation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterrogationRepository extends JpaRepository<Interrogation, Long> {

    List<Interrogation> findByGameSetAndNpcNameOrderByInterrogationNoDesc(GameSet gameSet, String npcName);

    void deleteByGameSet(GameSet gameSet);

}
