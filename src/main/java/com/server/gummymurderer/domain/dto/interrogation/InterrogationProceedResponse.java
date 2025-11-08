package com.server.gummymurderer.domain.dto.interrogation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.server.gummymurderer.domain.entity.Interrogation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterrogationProceedResponse {

    private String language;
    private String npcName;
    private String status;

    @JsonProperty("isMurderer")
    private boolean isMurderer;

    private int heartRate;
    private String response;

    public static InterrogationProceedResponse of(InterrogationProceedResponse ai, String language) {

        return new InterrogationProceedResponse(
                language,
                ai.getNpcName(),
                ai.getStatus(),
                ai.isMurderer(),
                ai.getHeartRate(),
                ai.getResponse()
        );
    }
}
