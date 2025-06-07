package com.server.gummymurderer.domain.dto.gameSetting;

import com.server.gummymurderer.domain.entity.GameSetting;
import com.server.gummymurderer.domain.enum_class.Language;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameSettingRequest {

    private float backgroundSoundVolume;
    private float effectSoundVolume;
    private String language;

    public GameSetting toEntity() {
        return GameSetting.builder()
                .backgroundSoundVolume(this.backgroundSoundVolume)
                .effectSoundVolume(this.effectSoundVolume)
                .language(Language.valueOf(this.language.toUpperCase()))
                .build();
    }

}
