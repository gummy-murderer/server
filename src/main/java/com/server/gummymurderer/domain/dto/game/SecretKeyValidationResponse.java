package com.server.gummymurderer.domain.dto.game;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SecretKeyValidationResponse {

    private String message;
    private String detail;
    private boolean valid;

}
