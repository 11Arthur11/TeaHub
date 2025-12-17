package dev.parhamziaei.teahub.dto.response.user.admin;

import dev.parhamziaei.teahub.dto.response.user.AbstractUserDetailResponse;
import dev.parhamziaei.teahub.dto.response.user.user.UserDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailAdminResponse extends AbstractUserDetailResponse {

    private Long id;

    private LocalDateTime updatedAt;

    private boolean emailVerified;

    private boolean enabled;

    private boolean expired;

    private boolean locked;

}
