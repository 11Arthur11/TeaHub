package dev.parhamziaei.teahub.dto.request.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UsersFilterRequest extends BasePaginationRequest {

    private Long byRoleId;
    private Boolean byEnabled;
    private Boolean byLocked;

}
