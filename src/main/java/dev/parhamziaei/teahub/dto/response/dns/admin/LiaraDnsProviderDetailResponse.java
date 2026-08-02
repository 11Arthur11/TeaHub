package dev.parhamziaei.teahub.dto.response.dns.admin;

import dev.parhamziaei.teahub.enums.dns.DnsProviderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LiaraDnsProviderDetailResponse {

    private Boolean active;

    private DnsProviderStatus status;

    private String baseUrl;

//    private String apiKey;

    private List<DnsZoneListResponse> dnsZones;

}
