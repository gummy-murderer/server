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
    @Pattern(regexp="^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*()_+=-]).{6,12}$", message = "비밀번호는 영어, 숫자, 특수문자 각각 최소 한 번 이상 포함해서 6~12자리 이내로 입력해주세요.")
    private String password;

    @NotBlank(message = "닉네임을 입력해주세요.")
//    @Pattern(regexp = "^[a-zA-Z0-9가-힣]+$", message = "닉네임은 숫자, 한글, 영어만 가능합니다.")
//    @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하여야합니다.")
    private String nickname;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "email을 입력해주세요.")
    @Email(message = "email 형식에 맞게 작성해주세요.")
    private String email;

    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .account(getAccount())
                .password(passwordEncoder.encode(getPassword()))
                .name(getName())
                .nickname(getNickname())
                .email(getEmail())
                .roles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()))
                .build();
    }

}