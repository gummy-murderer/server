package com.server.gummymurderer.service;

import com.server.gummymurderer.configuration.jwt.JwtProvider;
import com.server.gummymurderer.domain.dto.game.LoginGameSetDTO;
import com.server.gummymurderer.domain.dto.member.JoinMemberResponse;
import com.server.gummymurderer.domain.dto.member.LoginRequest;
import com.server.gummymurderer.domain.dto.member.SignRequest;
import com.server.gummymurderer.domain.dto.member.SignResponse;
import com.server.gummymurderer.domain.entity.Authority;
import com.server.gummymurderer.domain.entity.GameSet;
import com.server.gummymurderer.domain.entity.Member;
import com.server.gummymurderer.exception.AppException;
import com.server.gummymurderer.exception.ErrorCode;
import com.server.gummymurderer.repository.GameSetRepository;
import com.server.gummymurderer.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SignService {

    private final MemberRepository memberRepository;
    private final GameSetRepository gameSetRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public SignResponse login(LoginRequest request) throws Exception {
        Member member = memberRepository.findByAccount(request.getAccount()).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_ACCOUNT_OR_PASSWORD)
        );

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new AppException(ErrorCode.INVALID_ACCOUNT);
        }

        List<GameSet> memberGameSet = gameSetRepository.findGameSetsByMember(member);


        List<LoginGameSetDTO> loginGameSetDTOList = memberGameSet.stream()
                .map(LoginGameSetDTO::new) // Assuming LoginGameSetDTO has a constructor that accepts a GameSet
                .toList();


        return SignResponse.builder()
                .memberNo(member.getMemberNo())
                .account(member.getAccount())
                .name(member.getName())
                .email(member.getEmail())
                .roles(member.getRoles())
                .token(jwtProvider.createToken(member.getAccount(), member.getRoles()))
                .loginGameSetDTO(loginGameSetDTOList)
                .build();
    }

    public SignResponse register(SignRequest request) {

        // 계정이 중복될때 발생하는 에러
        if (memberRepository.findByAccount(request.getAccount()).isPresent()) {
            throw new AppException(ErrorCode.DUPLICATED_ACCOUNT);
        }

        // 이메일이 등록되어있을때 발생하는 에러
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.DUPLICATED_EMAIL);
        }

            Member member = Member.builder()
                    .account(request.getAccount())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .name(request.getName())
                    .nickname(request.getNickname())
                    .email(request.getEmail())
                    .build();

            member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));

            Member savedMember = memberRepository.save(member);


        return SignResponse.builder()
                .memberNo(savedMember.getMemberNo())
                .account(savedMember.getAccount())
                .name(savedMember.getName())
                .email(savedMember.getEmail())
                .roles(savedMember.getRoles())
                .build();
    }
}
