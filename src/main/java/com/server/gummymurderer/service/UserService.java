package com.server.gummymurderer.service;

import com.server.gummymurderer.domain.dto.user.ReadAllUserResponse;
import com.server.gummymurderer.domain.dto.user.JoinUserRequest;
import com.server.gummymurderer.domain.dto.user.JoinUserResponse;
import com.server.gummymurderer.domain.dto.user.ReadUserResponse;
import com.server.gummymurderer.domain.entity.User;
import com.server.gummymurderer.exception.AppException;
import com.server.gummymurderer.exception.ErrorCode;
import com.server.gummymurderer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public JoinUserResponse join(JoinUserRequest request) {

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

        return JoinUserResponse.builder()
                .userNo(savedUser.getUserNo())
                .userId(savedUser.getUserId())
                .userNickname(savedUser.getUserNickname())
                .build();
    }


    public Page<ReadAllUserResponse> readAllUser(PageRequest pageable) {

        return userRepository.findAll(pageable).map(ReadAllUserResponse::of);
    }

    public ReadUserResponse readByNo(long userNo) {

        User foundUser = userRepository.findByUserNo(userNo)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return ReadUserResponse.of(foundUser);
    }

    //가입을 요청한 닉네임으로 유저 조회 -있으면 DUPLICATED_NICKNAME에러발생
    private void validateUserByNickName(String userNickname) {
        userRepository.findByUserNickname(userNickname)
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.DUPLICATED_NICKNAME);
                });
    }

    //가입을 요청한 아이디로 유저 조회 -있으면 DUPLICATED_ID에러발생
    private void validateUserById(String userId) {
        userRepository.findByUserId(userId)
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.DUPLICATED_ID);
                });
    }
}
