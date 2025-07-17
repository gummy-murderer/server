package com.server.gummymurderer.repository;


import com.server.gummymurderer.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberNo(long memberNo);

    Optional<Member> findByNickname(String nickname);

    @Query("SELECT m FROM Member m WHERE m.steamId = :steamId")
    Optional<Member> findBySteamId(String steamId);

}
