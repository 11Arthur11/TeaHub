package dev.parhamziaei.teahub.dto.response.user.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEditAdminRequest {
    private String firstName;
    private String lastName;
    private String email;
}
