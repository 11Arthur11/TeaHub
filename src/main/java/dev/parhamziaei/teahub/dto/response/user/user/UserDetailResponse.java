package dev.parhamziaei.teahub.dto.response.user.user;

import dev.parhamziaei.teahub.dto.response.user.AbstractUserDetailResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class UserDetailResponse extends AbstractUserDetailResponse {}
