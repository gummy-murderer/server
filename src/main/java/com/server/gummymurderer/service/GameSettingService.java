package com.server.gummymurderer.service;

import com.server.gummymurderer.domain.dto.gameSetting.GameSettingRequest;
import com.server.gummymurderer.domain.entity.GameSetting;
import com.server.gummymurderer.domain.entity.Member;
import com.server.gummymurderer.domain.enum_class.Language;
import com.server.gummymurderer.exception.AppException;
import com.server.gummymurderer.exception.ErrorCode;
import com.server.gummymurderer.repository.GameSettingRepository;
import com.server.gummymurderer.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameSettingService {

    private final MemberRepository memberRepository;
    private final GameSettingRepository gameSettingRepository;

    public String settingSave(Member loginMember, GameSettingRequest request) {

        Member member = memberRepository.findByNickname(loginMember.getNickname())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_ACCOUNT));

        log.info("language : {}", request.getLanguage());

        GameSetting gameSetting = gameSettingRepository.findByMemberNo(member.getMemberNo())
                .map(existing -> {
                    // 업데이트
                    existing.update(
                            request.getBackgroundSoundVolume(),
                            request.getEffectSoundVolume(),
                            Language.valueOf(request.getLanguage().toUpperCase())
                    );
                    return existing;
                })
                .orElseGet(() -> {
                    // 새로 생성
                    GameSetting newSetting = request.toEntity();
                    newSetting.assignMember(member);
                    return newSetting;
                });

        gameSettingRepository.save(gameSetting);

        return "Game Setting 저장 완료";
    }

}
