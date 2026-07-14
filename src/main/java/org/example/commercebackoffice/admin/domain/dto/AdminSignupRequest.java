package org.example.commercebackoffice.admin.domain.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import org.example.commercebackoffice.admin.domain.enums.AdminRole;

@Getter
public class AdminSignupRequest {
    @NotBlank(message = "이름은 필수로 입력해야됩니다")
    private String name;

    @NotBlank(message = "이메일은 필수로 입력해야됩니다")
    @Email(message =  "올바른 이메일 형식이 아닙니다" )
    private String email;

    @NotBlank(message = "비밀번호는 필수로 입력해야됩니다")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상")
    private String password;

    @NotBlank(message = "전화번호는 필수로 입력해야됩니다")
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$" , message = "전화번호는 010-0000-0000 형식이어야 됩니다")
    private String phoneNumber;

    @NotNull(message = "역할은 필수로 선택해야됩니다")
    private AdminRole role; //super, operator, cs 등
}
