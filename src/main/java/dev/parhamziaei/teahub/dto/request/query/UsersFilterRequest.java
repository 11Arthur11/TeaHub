package dev.parhamziaei.teahub.dto.request.query;

import dev.parhamziaei.teahub.entity.jpa.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.PageRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class UsersFilterRequest extends AbstractPaginationRequest {

    private Long byRoleId;
    private Boolean byEnabled;
    private Boolean byLocked;

}
