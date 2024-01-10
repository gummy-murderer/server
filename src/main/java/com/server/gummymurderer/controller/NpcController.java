package com.server.gummymurderer.controller;

import com.server.gummymurderer.domain.dto.npc.*;
import com.server.gummymurderer.domain.dto.user.ReadAllUserResponse;
import com.server.gummymurderer.exception.Response;
import com.server.gummymurderer.service.NpcService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/npc")
@RequiredArgsConstructor
@Slf4j
public class NpcController {

    private final NpcService npcService;

    @Operation(summary = "npc 등록", description = "npcName, npcPersonality, npcFeature 필요")
    @PostMapping("/enroll")
    public ResponseEntity<Response<EnrollNpcResponse>> enroll(@RequestBody EnrollNpcRequest request) {

        EnrollNpcResponse npcCreateResponse = npcService.enroll(request);
        return ResponseEntity.ok(Response.success(npcCreateResponse));
    }

    @Operation(summary = "npc 수정", description = "npcName, npcPersonality, npcFeature 필요")
    @PostMapping("/update/{npcNo}")
    public ResponseEntity<Response<UpdateNpcResponse>> update(@RequestBody UpdateNpcRequest request, @PathVariable long npcNo) {

        UpdateNpcResponse npcUpdateResponse = npcService.update(request, npcNo);

        return ResponseEntity.ok(Response.success(npcUpdateResponse));
    }

    @Operation(summary = "npc 전체조회", description = "모든 npc의 No, Name, Personality, Feature, CreatedAt, LastModifiedAt, DeletedAt 이 조회됩니다")
    @GetMapping("read/all")
    public ResponseEntity<Response<Page<ReadAllNpcResponse>>> readAll() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by("npcNo").descending());

        Page<ReadAllNpcResponse> readAllUserResponsePage = npcService.readAll(pageable);

        return  ResponseEntity.ok(Response.success(readAllUserResponsePage));
    }

    @Operation(summary = "npc 번호로 조회", description = "특정 npc의 No으로 No, Name, Personality, Feature, CreatedAt, LastModifiedAt, DeletedAt 이 조회됩니다")
    @GetMapping("read/no/{npcNo}")
    public ResponseEntity<Response<ReadNpcResponse>> readByNo(@PathVariable long npcNo) {
        ReadNpcResponse readNpcResponse = npcService.readByNo(npcNo);

        return ResponseEntity.ok(Response.success(readNpcResponse));
    }

    @Operation(summary = "npc 이름으로 조회", description = "특정 npc의 이름으로 No, Name, Personality, Feature, CreatedAt, LastModifiedAt, DeletedAt 이 조회됩니다")
    @GetMapping("read/name/{npcName}")
    public ResponseEntity<Response<ReadNpcResponse>> readByName(@PathVariable String npcName) {
        ReadNpcResponse readNpcResponse = npcService.readByName(npcName);

        return ResponseEntity.ok(Response.success(readNpcResponse));
    }
}
