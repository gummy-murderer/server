package com.server.gummymurderer.service;

import com.server.gummymurderer.configuration.jwt.JwtProvider;
import com.server.gummymurderer.domain.dto.game.LoginGameSetDTO;
import com.server.gummymurderer.domain.dto.member.*;
import com.server.gummymurderer.domain.entity.GameSet;
import com.server.gummymurderer.domain.entity.GameUserCustom;
import com.server.gummymurderer.domain.entity.Member;
import com.server.gummymurderer.exception.AppException;
import com.server.gummymurderer.exception.ErrorCode;
import com.server.gummymurderer.repository.GameSetRepository;
import com.server.gummymurderer.repository.GameUserCustomRepository;
import com.server.gummymurderer.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class SignService {

    private final MemberRepository memberRepository;
    private final GameSetRepository gameSetRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final GameUserCustomRepository gameUserCustomRepository;

    public SignResponse login(LoginRequest request) throws Exception {

        log.info("🐻login 시작");

        Member member = memberRepository.findByAccount(request.getAccount()).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ACCOUNT_OR_PASSWORD)
        );

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new AppException(ErrorCode.INVALID_ACCOUNT);
        }

        List<GameSet> memberGameSet = gameSetRepository.findGameSetsByMember(member);

        List<LoginGameSetDTO> loginGameSetDTOList = memberGameSet.stream()
                .map(gameSet -> {
                    GameUserCustom custom = gameUserCustomRepository.findByGameSet(gameSet).orElse(null);
                    return new LoginGameSetDTO(gameSet, custom);
                })
                .toList();

        log.info("🐻loginGameSetDTO : {}", loginGameSetDTOList);

        String token = jwtProvider.createToken(member.getAccount(), member.getRoles());

        return SignResponse.of(member, loginGameSetDTOList, token);
    }

    public SignResponse register(SignRequest request) {

        log.info("🐻account : {}", request.getAccount());
        log.info("🐻nickName : {}", request.getNickname());

        // 계정이 중복될때 발생하는 에러
        if (memberRepository.findByAccount(request.getAccount()).isPresent()) {
            throw new AppException(ErrorCode.DUPLICATED_ACCOUNT);
        }

        // nickName이 중복 될 때 발생하는 에러
        if (memberRepository.findByNickname(request.getNickname()).isPresent()) {
            throw new AppException(ErrorCode.DUPLICATED_NICKNAME);
        }

        Member member = request.toEntity(passwordEncoder);

        Member savedMember = memberRepository.save(member);

        return SignResponse.of(savedMember);
    }

    public DuplicatedResponse duplicateCheckAccount(DuplicatedAccountRequest request) {

        if (memberRepository.findByAccount(request.getAccount()).isPresent()) {
            throw new AppException(ErrorCode.DUPLICATED_ACCOUNT);
        } else {
            return new DuplicatedResponse("사용 가능한 Account 입니다.");
        }
    }

    public DuplicatedResponse duplicateCheckNickname(DuplicatedNicknameRequest request) {

        if (memberRepository.findByNickname(request.getNickname()).isPresent()) {
            throw new AppException(ErrorCode.DUPLICATED_NICKNAME);
        } else {
            return new DuplicatedResponse("사용 가능한 NickName 입니다.");
        }
    }

}
