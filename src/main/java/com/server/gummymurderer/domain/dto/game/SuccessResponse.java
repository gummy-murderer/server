package com.server.gummymurderer.domain.dto.game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessResponse {

    private String message;
    private Boolean valid;

}
