package dev.parhamziaei.teahub.dto.response.user;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class AbstractUserDetailResponse {

    private String phone;

    private String firstName;

    private String lastName;

    private String email;

    private String role;

    private LocalDateTime lastLogin;

    private LocalDateTime createdAt;

    private boolean emailVerified;

}
