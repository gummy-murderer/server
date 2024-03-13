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

        String token = jwtProvider.createToken(member.getAccount(), member.getRoles());

        return SignResponse.of(member, loginGameSetDTOList, token);
    }

    public SignResponse register(SignRequest request) {

        log.info("🐻account : {}", request.getAccount());
        log.info("🐻name : {}", request.getName());
        log.info("🐻nickName : {}", request.getNickname());
        log.info("🐻email : {}", request.getEmail());

        String nickName = request.getNickname().replace("\u200B", "");

        // 닉네임 길이 체크
        if (nickName.length() < 2 || nickName.length() > 10) {
            throw new AppException(ErrorCode.INVALID_NICKNAME_LENGTH);
        }

        // 닉네임 패턴 체크
        if (!nickName.matches("^[a-zA-Z0-9가-힣]+$")) {
            throw new AppException(ErrorCode.INVALID_NICKNAME_PATTERN);
        }

        // 닉네임의 각 문자와 그 문자의 유니코드 값을 출력하는 로그 추가
        for (char ch : nickName.toCharArray()) {

            int unicode = (int) ch;

            log.info("🐻Character: {}, Unicode: {}", ch, unicode);

            // 영어, 숫자, 한글 범위에 속하지 않는 경우 로그 출력
            if(!(('a' <= unicode && unicode <= 'z') ||
                 ('A' <= unicode && unicode <= 'Z') ||
                 ('0' <= unicode && unicode <= '9') ||
                 ('가' <= unicode && unicode <= '힣'))) {
                log.warn("🐻Invalid character: {}, Unicode: {}", ch, unicode);
            }
        }

        request.setNickname(nickName);

        log.info("🐻nickName : {}", request.getNickname());

        // 계정이 중복될때 발생하는 에러
        if (memberRepository.findByAccount(request.getAccount()).isPresent()) {
            throw new AppException(ErrorCode.DUPLICATED_ACCOUNT);
        }

        // 이메일이 등록되어있을때 발생하는 에러
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.DUPLICATED_EMAIL);
        }

        // nickName이 중복 될 때 발생하는 에러
        if (memberRepository.findByNickname(request.getNickname()).isPresent()) {
            throw new AppException(ErrorCode.DUPLICATED_NICKNAME);
        }

        Member member = request.toEntity(passwordEncoder);

        Member savedMember = memberRepository.save(member);

        return SignResponse.of(savedMember);
    }

    public String duplicateCheckAccount(DuplicatedAccountRequest request) {
        log.info("Checking for duplicate account: {}", request);

        if (memberRepository.findByAccount(request.getAccount()).isPresent()) {
            throw new AppException(ErrorCode.DUPLICATED_ACCOUNT);
        } else {
            return "사용 가능한 Account 입니다.";
        }
    }


    public String duplicateCheckEmail(DuplicatedEmailRequest request) {
        if (memberRepository.findByAccount(request.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.DUPLICATED_EMAIL);
        } else {
            return "사용 가능한 Email 입니다.";
        }
    }

    public String duplicateCheckNickname(DuplicatedNicknameRequest request) {
        if (memberRepository.findByAccount(request.getNickname()).isPresent()) {
            throw new AppException(ErrorCode.DUPLICATED_NICKNAME);
        } else {
            return "사용 가능한 NickName 입니다.";
        }
    }

}
