package dev.parhamziaei.teahub.entity.redis;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PhoneVerifySession implements Serializable {

    private String phoneNumber;
    private String code;
    private int attempts;
    private boolean verified;

    public PhoneVerifySession(String phoneNumber, String hashedCode) {
        this.phoneNumber = phoneNumber;
        this.code = hashedCode;
        this.attempts = 0;
    }

}
