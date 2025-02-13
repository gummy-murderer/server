package com.server.gummymurderer.domain.entity;

import com.server.gummymurderer.domain.dto.gameUserDetectiveNotebook.DetectiveNotebookRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "game_user_detective_notebook_tb")
public class GameUserDetectiveNotebook extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_detective_notebook_no")
    private Long userDetectiveNotebookNo;

    @Column(name = "time")
    private String time;

    @Column(name = "place")
    private String place;

    @Column(name = "weapon")
    private String weapon;

    @Column(name = "contents")
    private String contents;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_npc_no")
    private GameNpc gameNpc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_set_no")
    private GameSet gameSet;

    public void updateNotebook(DetectiveNotebookRequest request) {
        this.time = listToString(request.getTime());
        this.place = listToString(request.getPlace());
        this.weapon = listToString(request.getWeapon());
        this.contents = request.getContents();
    }

    // 저장 시 List<Integer> → 쉼표로 변환
    public static String listToString(List<Integer> list) {
        return list != null ? list.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(",")) : "";
    }

    // 조회 시 쉼표 문자열 → List<Integer> 변환
    public static List<Integer> stringToList(String str) {
        if (str == null || str.isEmpty()) return Collections.emptyList();
        return Arrays.stream(str.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
