package dev.parhamziaei.teahub.dto.request.authentication;

import dev.parhamziaei.teahub.validation.annotation.PhoneNumber;
import lombok.Data;

@Data
public class AuthEntryRequest {

    @PhoneNumber
    String phoneNumber;

}
