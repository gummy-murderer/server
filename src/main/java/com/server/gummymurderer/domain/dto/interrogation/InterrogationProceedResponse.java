package com.server.gummymurderer.domain.dto.interrogation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterrogationProceedResponse {

    private String response;
    private Integer heartRate;

}
