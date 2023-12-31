package com.server.gummymurderer.repository;


import com.server.gummymurderer.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);
    Optional<User> findByUserNickname(String userNickname);
    Optional<User> findByUserNo(long userNo);
    
    Page<User> findAll(Pageable pageable);
}
