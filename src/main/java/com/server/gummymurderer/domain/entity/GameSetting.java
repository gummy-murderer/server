package com.server.gummymurderer.domain.entity;

import com.server.gummymurderer.domain.enum_class.Language;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Table(name = "game_setting_tb")
public class GameSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameSettingNo;

    private float backgroundSoundVolume;
    private float effectSoundVolume;

    @Enumerated(EnumType.STRING)
    private Language language;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    public void assignMember(Member member) {
        this.member = member;
    }

}
