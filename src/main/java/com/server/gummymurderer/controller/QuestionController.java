package com.server.gummymurderer.controller;

import com.server.gummymurderer.domain.dto.question.QuestionAnswerRequest;
import com.server.gummymurderer.domain.dto.question.QuestionAnswerResponse;
import com.server.gummymurderer.domain.dto.question.QuestionCreateRequest;
import com.server.gummymurderer.domain.dto.question.QuestionCreateResponse;
import com.server.gummymurderer.domain.entity.Member;
import com.server.gummymurderer.exception.Response;
import com.server.gummymurderer.service.CustomUserDetails;
import com.server.gummymurderer.service.QuestionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/question")
@RequiredArgsConstructor
@Slf4j
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/create")
    public Response<QuestionCreateResponse> createQuestion(@RequestBody QuestionCreateRequest request, @AuthenticationPrincipal CustomUserDetails customUserDetails, HttpServletRequest httpServletRequest) {

        Member loginMember = customUserDetails.getMember();

        QuestionCreateResponse response = questionService.createQuestion(loginMember, request, httpServletRequest);
        return Response.success(response);

    }

    @PostMapping("/answer")
    public Response<QuestionAnswerResponse> answerQuestion(@RequestBody QuestionAnswerRequest request, @AuthenticationPrincipal CustomUserDetails customUserDetails, HttpServletRequest httpServletRequest) {

        Member loginMember = customUserDetails.getMember();

        QuestionAnswerResponse response = questionService.getAnswer(loginMember, request, httpServletRequest);
        return Response.success(response);
    }
}
