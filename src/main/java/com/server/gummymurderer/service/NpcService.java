package com.server.gummymurderer.service;

import com.server.gummymurderer.domain.dto.npc.NpcEnrollRequest;
import com.server.gummymurderer.domain.dto.npc.NpcEnrollResponse;
import com.server.gummymurderer.domain.entity.Npc;
import com.server.gummymurderer.repository.NpcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NpcService {

    private final NpcRepository npcRepository;

    @Transactional
    public NpcEnrollResponse enroll(NpcEnrollRequest request) {

       Npc savedNpc = npcRepository.save(Npc.builder()
               .npcName(request.getNpcName())
               .npcPersonality(request.getNpcPersonality())
               .npcFeature(request.getNpcFeature())
               .build());

        return new NpcEnrollResponse(savedNpc);
    }
}
