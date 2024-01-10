package com.server.gummymurderer.controller;

import com.server.gummymurderer.domain.dto.npc.NpcEnrollRequest;
import com.server.gummymurderer.domain.dto.npc.NpcEnrollResponse;
import com.server.gummymurderer.exception.Response;
import com.server.gummymurderer.service.NpcService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/npc")
@RequiredArgsConstructor
@Slf4j
public class NpcController {

    private final NpcService npcService;

    @PostMapping("/enroll")
    public ResponseEntity<Response<NpcEnrollResponse>> enroll(@RequestBody NpcEnrollRequest request) {

        NpcEnrollResponse npcCreateResponse = npcService.enroll(request);
        return ResponseEntity.ok(Response.success(npcCreateResponse));
    }

}
