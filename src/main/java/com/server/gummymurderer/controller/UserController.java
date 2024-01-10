package com.server.gummymurderer.controller;

import com.server.gummymurderer.domain.dto.user.JoinUserRequest;
import com.server.gummymurderer.domain.dto.user.JoinUserResponse;
import com.server.gummymurderer.domain.dto.user.ReadAllUserResponse;
import com.server.gummymurderer.domain.dto.user.ReadUserResponse;
import com.server.gummymurderer.exception.Response;
import com.server.gummymurderer.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;


    @PostMapping("users/signup")
    public ResponseEntity<Response<JoinUserResponse>> create(@RequestBody JoinUserRequest request) {

        JoinUserResponse joinResponse = userService.join(request);
        return ResponseEntity.ok(Response.success(joinResponse));
    }

    @GetMapping("users/{userNo}")
    public ResponseEntity<Response<ReadUserResponse>> readByUserNickname(@PathVariable long userNo) {

        ReadUserResponse readUserResponse = userService.readByNo(userNo);

        return ResponseEntity.ok(Response.success(readUserResponse));
    }

    @GetMapping("users")
    public ResponseEntity<Response<Page<ReadAllUserResponse>>> readAll() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("userNo").descending());

        Page<ReadAllUserResponse> readAllUserResponsePage = userService.readAllUser(pageable);

        return ResponseEntity.ok(Response.success(readAllUserResponsePage));
    }
}