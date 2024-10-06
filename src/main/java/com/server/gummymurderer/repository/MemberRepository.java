package com.server.gummymurderer.repository;


import com.server.gummymurderer.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByAccount(String account);

    Optional<Member> findByMemberNo(long memberNo);

    Optional<Member> findByNickname(String nickname);

}
