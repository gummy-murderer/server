package com.server.gummymurderer.service;

import com.server.gummymurderer.domain.dto.gameSetting.GameSettingRequest;
import com.server.gummymurderer.domain.entity.GameSetting;
import com.server.gummymurderer.domain.entity.Member;
import com.server.gummymurderer.exception.AppException;
import com.server.gummymurderer.exception.ErrorCode;
import com.server.gummymurderer.repository.GameSettingRepository;
import com.server.gummymurderer.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameSettingService {

    private final MemberRepository memberRepository;
    private final GameSettingRepository gameSettingRepository;

    public String settingSave(Member loginMember, GameSettingRequest request) {

        Member member = memberRepository.findByNickname(loginMember.getNickname())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_ACCOUNT));

        GameSetting gameSetting = request.toEntity();
        gameSetting.assignMember(member);

        gameSettingRepository.save(gameSetting);

        return "Game Setting 저장 완료";
    }

}
