package com.server.gummymurderer.domain.dto.gameSetting;

import com.server.gummymurderer.domain.enum_class.Language;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameSettingDTO {

    private float backgroundSoundVolume;
    private float effectSoundVolume;
    private Language language;
}
