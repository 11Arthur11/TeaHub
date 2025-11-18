package dev.parhamziaei.teahub.dto.request.authentication;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class LoginRequest {

    @Length(min = 6, max = 6)
    private String twoFactorCode;

    private boolean rememberMe;

}
