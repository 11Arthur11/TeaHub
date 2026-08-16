package dev.parhamziaei.teahub.dto.response.resource.teaspeak.admin;

import dev.parhamziaei.teahub.dto.response.resource.teaspeak.user.TeaSpeakResourceDetailResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TeaSpeakResourceDetailAdminResponse extends TeaSpeakResourceDetailResponse {

    private Long ownerId;
    private String sid;

}
