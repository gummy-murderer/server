package com.server.gummymurderer.controller;

import com.server.gummymurderer.domain.dto.user.UserJoinRequest;
import com.server.gummymurderer.domain.dto.user.UserJoinResponse;
import com.server.gummymurderer.exception.Response;
import com.server.gummymurderer.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;


    @PostMapping("/signup")
    public ResponseEntity<Response<UserJoinResponse>> create(@RequestBody UserJoinRequest request) {

        UserJoinResponse joinResponse = userService.join(request);
        return ResponseEntity.ok(Response.success(joinResponse));
    }
}