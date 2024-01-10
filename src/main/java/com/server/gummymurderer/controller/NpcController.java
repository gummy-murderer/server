package com.server.gummymurderer.controller;

import com.server.gummymurderer.domain.dto.npc.*;
import com.server.gummymurderer.domain.dto.user.ReadAllUserResponse;
import com.server.gummymurderer.exception.Response;
import com.server.gummymurderer.service.NpcService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/npc")
@RequiredArgsConstructor
@Slf4j
public class NpcController {

    private final NpcService npcService;

    @PostMapping("/enroll")
    public ResponseEntity<Response<EnrollNpcResponse>> enroll(@RequestBody EnrollNpcRequest request) {

        EnrollNpcResponse npcCreateResponse = npcService.enroll(request);
        return ResponseEntity.ok(Response.success(npcCreateResponse));
    }

    @PostMapping("/update/{npcNo}")
    public ResponseEntity<Response<UpdateNpcResponse>> update(@RequestBody UpdateNpcRequest request, @PathVariable long npcNo) {

        UpdateNpcResponse npcUpdateResponse = npcService.update(request, npcNo);

        return ResponseEntity.ok(Response.success(npcUpdateResponse));
    }

    @GetMapping("read/all")
    public ResponseEntity<Response<Page<ReadAllNpcResponse>>> readAll() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("npcNo").descending());

        Page<ReadAllNpcResponse> readAllUserResponsePage = npcService.readAll(pageable);

        return  ResponseEntity.ok(Response.success(readAllUserResponsePage));
    }

    @GetMapping("read/no/{npcNo}")
    public ResponseEntity<Response<ReadNpcResponse>> readByNo(@PathVariable long npcNo) {
        ReadNpcResponse readNpcResponse = npcService.readByNo(npcNo);

        return ResponseEntity.ok(Response.success(readNpcResponse));
    }
}
