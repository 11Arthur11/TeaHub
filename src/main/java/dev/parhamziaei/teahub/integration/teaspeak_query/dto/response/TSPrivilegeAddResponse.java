package dev.parhamziaei.teahub.integration.teaspeak_query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TSPrivilegeAddResponse extends BaseQueryResponse{

    private String token;
    private String token_id;

}
