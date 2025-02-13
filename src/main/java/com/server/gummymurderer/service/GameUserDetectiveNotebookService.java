package com.server.gummymurderer.service;

import com.server.gummymurderer.domain.dto.gameUserDetectiveNotebook.DetectiveNotebookRequest;
import com.server.gummymurderer.domain.dto.gameUserDetectiveNotebook.DetectiveNotebookSaveRequest;
import com.server.gummymurderer.domain.dto.gameUserDetectiveNotebook.DetectiveNotebookSaveResponse;
import com.server.gummymurderer.domain.entity.GameNpc;
import com.server.gummymurderer.domain.entity.GameSet;
import com.server.gummymurderer.domain.entity.GameUserDetectiveNotebook;
import com.server.gummymurderer.domain.entity.Member;
import com.server.gummymurderer.exception.AppException;
import com.server.gummymurderer.exception.ErrorCode;
import com.server.gummymurderer.repository.GameNpcRepository;
import com.server.gummymurderer.repository.GameSetRepository;
import com.server.gummymurderer.repository.GameUserDetectiveNotebookRepository;
import com.server.gummymurderer.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameUserDetectiveNotebookService {

    private final GameUserDetectiveNotebookRepository gameUserDetectiveNotebookRepository;
    private final GameNpcRepository gameNpcRepository;
    private final GameSetRepository gameSetRepository;
    private final MemberRepository memberRepository;

    public List<DetectiveNotebookSaveResponse> saveOrUpdateNotebook(Member loginMember, DetectiveNotebookSaveRequest request) {

        log.info("📌 [START] saveOrUpdateNotebook() 실행");
        log.info("📝 Notebook GameSetNo: {}", request.getGameSetNo());
        log.info("📝 Notebook RequestList: {}", request.getNotebookRequestList());

        if (request.getNotebookRequestList() == null) {
            log.error("🚨 notebookRequestList is NULL! 클라이언트에서 데이터를 보내지 않았음.");
        } else if (request.getNotebookRequestList().isEmpty()) {
            log.warn("⚠️ notebookRequestList is EMPTY! 클라이언트가 빈 리스트([])를 보냈음.");
        } else {
            log.info("✅ notebookRequestList contains {} items.", request.getNotebookRequestList().size());
        }


        Member member = memberRepository.findByNickname(loginMember.getNickname())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_ACCOUNT));

        GameSet gameSet = gameSetRepository.findByGameSetNo(request.getGameSetNo())
                .orElseThrow(() -> new AppException(ErrorCode.GAME_SET_NOT_FOUND));

        List<DetectiveNotebookSaveResponse> responses = new ArrayList<>();

        for (DetectiveNotebookRequest notebookRequest : request.getNotebookRequestList()) {

            // 📌 로그 추가: 현재 처리 중인 요청 데이터 확인
            log.info("🕵️ Processing NPC: {}", notebookRequest.getNpcName());
            log.info("   🕒 Time: {}", notebookRequest.getTime());
            log.info("   📍 Place: {}", notebookRequest.getPlace());
            log.info("   🔪 Weapon: {}", notebookRequest.getWeapon());
            log.info("   ✍️ Contents: {}", notebookRequest.getContents());

            GameNpc gameNpc = gameNpcRepository.findByNpcNameAndGameSet(notebookRequest.getNpcName(), gameSet)
                    .orElseThrow(() -> new AppException(ErrorCode.NPC_NOT_FOUND));

            // 기존 노트가 있는지 확인
            Optional<GameUserDetectiveNotebook> existingNotebook =
                    gameUserDetectiveNotebookRepository.findByGameNpcAndGameSet(gameNpc, gameSet);

            GameUserDetectiveNotebook gameUserDetectiveNotebook;
            if (existingNotebook.isPresent()) {
                // 기존 데이터 업데이트
                gameUserDetectiveNotebook = existingNotebook.get();
                gameUserDetectiveNotebook.updateNotebook(notebookRequest);
            } else {
                // 새로운 데이터 저장
                gameUserDetectiveNotebook = notebookRequest.toEntity(gameNpc, gameSet);
            }

            log.info("📌 리스트 변환 결과 확인");
            log.info("   🕒 Time(String): {}", GameUserDetectiveNotebook.listToString(notebookRequest.getTime()));
            log.info("   📍 Place(String): {}", GameUserDetectiveNotebook.listToString(notebookRequest.getPlace()));
            log.info("   🔪 Weapon(String): {}", GameUserDetectiveNotebook.listToString(notebookRequest.getWeapon()));

            gameUserDetectiveNotebook = gameUserDetectiveNotebookRepository.save(gameUserDetectiveNotebook);
            responses.add(DetectiveNotebookSaveResponse.of(gameUserDetectiveNotebook));

            log.info("✅ 노트 저장 완료 (ID: {})", gameUserDetectiveNotebook.getUserDetectiveNotebookNo());

        }
        return responses;
    }

    public List<DetectiveNotebookSaveResponse> getNotebookList (GameSet gameSet) {
        return gameUserDetectiveNotebookRepository.findByGameNpc_GameSet(gameSet)
                .stream()
                .map(DetectiveNotebookSaveResponse::of)
                .toList();
    }

}