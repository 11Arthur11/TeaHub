package dev.parhamziaei.teahub.dto.request.authentication;

import dev.parhamziaei.teahub.validation.annotation.PasswordValidation;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class ChangePasswordRequest {

    @NotBlank
    private String oldPassword;

    @PasswordValidation
    private String newPassword;

    @NotBlank(message = "PASSWORD_CONFIRM_EMPTY")
    private String newPasswordConfirm;

}
