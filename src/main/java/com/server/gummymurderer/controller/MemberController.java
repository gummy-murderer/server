package com.server.gummymurderer.controller;

import com.server.gummymurderer.domain.dto.member.ReadAllMemberResponse;
import com.server.gummymurderer.domain.dto.member.ReadMemberResponse;
import com.server.gummymurderer.exception.Response;
import com.server.gummymurderer.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping("members/{memberNo}")
    public ResponseEntity<Response<ReadMemberResponse>> readByMemberNickname(@PathVariable long memberNo) {

        ReadMemberResponse readMemberResponse = memberService.readByNo(memberNo);

        return ResponseEntity.ok(Response.success(readMemberResponse));
    }

    @GetMapping("members")
    public ResponseEntity<Response<Page<ReadAllMemberResponse>>> readAll() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("memberNo").descending());

        Page<ReadAllMemberResponse> readAllMemberResponsePage = memberService.readAllMember(pageable);

        return ResponseEntity.ok(Response.success(readAllMemberResponsePage));
    }

}