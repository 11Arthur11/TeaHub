package dev.parhamziaei.teahub.dto.request.authentication;

import dev.parhamziaei.teahub.validation.annotation.PhoneNumber;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

@Data
public class AuthEntryRequest {

    @PhoneNumber
    @Parameter(
            name = "phone",
            description = "User's phone number for authentication initiation, must start with +98",
            required = true,
            example = "+989123456789"
    )
    String phoneNumber;

}
