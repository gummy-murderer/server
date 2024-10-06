package com.server.gummymurderer.domain.dto.member;

import com.server.gummymurderer.domain.entity.Authority;
import com.server.gummymurderer.domain.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignRequest {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String account;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .account(getAccount())
                .password(passwordEncoder.encode(getPassword()))
                .nickname(getNickname())
                .roles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()))
                .build();
    }
}