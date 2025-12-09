package dev.parhamziaei.teahub.integration.teaspeak_query.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TSPrivilegeAddResponse extends BaseQueryResponse{

    private String token;
    private String tokenId;

}
