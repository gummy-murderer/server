package com.server.gummymurderer.service;

import com.server.gummymurderer.domain.dto.member.ReadAllMemberResponse;
import com.server.gummymurderer.domain.dto.member.ReadMemberResponse;
import com.server.gummymurderer.domain.entity.Member;
import com.server.gummymurderer.exception.AppException;
import com.server.gummymurderer.exception.ErrorCode;
import com.server.gummymurderer.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    public Page<ReadAllMemberResponse> readAllMember(PageRequest pageable) {

        return memberRepository.findAll(pageable).map(ReadAllMemberResponse::of);
    }

    public ReadMemberResponse readByNo(long memberNo) {

        Member foundMember = memberRepository.findByMemberNo(memberNo)
                .orElseThrow(() -> new AppException(ErrorCode.MEMBER_NOT_FOUND));

        return ReadMemberResponse.of(foundMember);
    }

    //가입을 요청한 닉네임으로 유저 조회 -있으면 DUPLICATED_NICKNAME에러발생
    private void validateMemberByNickName(String memberNickname) {
        memberRepository.findByNickname(memberNickname)
                .ifPresent(member -> {
                    throw new AppException(ErrorCode.DUPLICATED_NICKNAME);
                });
    }

    //가입을 요청한 아이디로 유저 조회 -있으면 DUPLICATED_ID에러발생
    private void validateMemberById(String account) {
        memberRepository.findByAccount(account)
                .ifPresent(member -> {
                    throw new AppException(ErrorCode.DUPLICATED_ACCOUNT);
                });
    }
}
