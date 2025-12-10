package dev.parhamziaei.teahub.integration.teaspeak_query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TSPrivilegeListResponse {
    private String token;
    private String token_id;
    private String token_use_count;
    private String token_description;
}
