package com.server.gummymurderer.repository;

import com.server.gummymurderer.domain.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    Optional<Question> findTopByGameSet_GameSetNoAndNpcNameOrderByCreatedAtDesc(Long gameSetNo, String npcName);
}
