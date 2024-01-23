package com.server.gummymurderer.domain.dto.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignRequest {

    private String account;

    private String password;

    private String nickname;

    private String name;

    private String email;

}