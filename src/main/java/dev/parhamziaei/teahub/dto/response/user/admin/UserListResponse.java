package dev.parhamziaei.teahub.dto.response.user.admin;

import dev.parhamziaei.teahub.enums.user.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListResponse {

    private Long id;

    private String phone;

    private String fullName;

    private String email;

    private Roles role;

    private LocalDateTime lastLogin;

    private boolean online;

}
