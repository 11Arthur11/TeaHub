package dev.parhamziaei.teahub.dto.request.dns.user;

import dev.parhamziaei.teahub.validation.annotation.Subdomain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignSubdomainRequest {

    private Long zoneId;
    @Subdomain
    private String subdomain;
    private Long teaSpeakResourceId;

}
