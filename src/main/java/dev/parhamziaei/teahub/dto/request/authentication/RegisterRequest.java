package dev.parhamziaei.teahub.dto.request.authentication;

import dev.parhamziaei.teahub.validation.annotation.PasswordValidation;
import dev.parhamziaei.teahub.validation.annotation.PhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class RegisterRequest {

    @PhoneNumber
    private String phoneNumber;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @PasswordValidation
    private String password;

    @NotBlank
    private String passwordConfirm;

}
