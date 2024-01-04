package com.server.gummymurderer.service;

import com.server.gummymurderer.domain.dto.user.UserJoinRequest;
import com.server.gummymurderer.domain.dto.user.UserJoinResponse;
import com.server.gummymurderer.domain.entity.User;
import com.server.gummymurderer.exception.AppException;
import com.server.gummymurderer.exception.ErrorCode;
import com.server.gummymurderer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public UserJoinResponse join(UserJoinRequest request) {

        String userId = request.getUserId();
        String userNickName = request.getUserNickname();

//가입을 요청한 아이디로 유저 조회 -있으면 DUPLICATED_ID에러발생
        validateUserById(userId);
//가입을 요청한 닉네임으로 유저 조회 -있으면 DUPLICATED_NICKNAME에러발생
        validateUserByNickName(userNickName);

        String encryptedPassword = bCryptPasswordEncoder.encode(request.getUserPassword());

        User JoinUser = User.builder()
                .userId(userId)
                .userPassword(encryptedPassword)
                .userNickname(userNickName)
                .build();

        User savedUser = userRepository.save(JoinUser);

        return UserJoinResponse.builder()
                .userNo(savedUser.getUserNo())
                .userId(savedUser.getUserId())
                .userNickname(savedUser.getUserNickname())
                .build();
    }


    private void validateUserByNickName(String userNickname) {
        userRepository.findByUserNickname(userNickname)
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.DUPLICATED_NICKNAME);
                });
    }

    // userId로 User를 조회 -있을시 DUPLICATED_ID에러 발생
    private void validateUserById(String userId) {
        userRepository.findByUserId(userId)
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.DUPLICATED_ID);
                });
    }
}
