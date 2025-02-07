package com.server.gummymurderer.domain.dto.interrogation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterrogationProceedResponse {

    private String npcName;
    private String status;

    @JsonProperty("isMurderer")
    private boolean isMurderer;

    private int heartRate;
    private String response;

}
