package com.server.gummymurderer.controller;

import com.server.gummymurderer.domain.dto.npc.UpdateNpcRequest;
import com.server.gummymurderer.domain.dto.npc.UpdateNpcResponse;
import com.server.gummymurderer.domain.dto.npc.EnrollNpcRequest;
import com.server.gummymurderer.domain.dto.npc.EnrollNpcResponse;
import com.server.gummymurderer.exception.Response;
import com.server.gummymurderer.service.NpcService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        UpdateNpcResponse npcUpdateResponse = npcService.update(request,npcNo);

        return ResponseEntity.ok(Response.success(npcUpdateResponse));
    }
}
