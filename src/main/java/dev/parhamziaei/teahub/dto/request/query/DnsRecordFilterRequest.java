package dev.parhamziaei.teahub.dto.request.query;

import dev.parhamziaei.teahub.enums.dns.DnsProviderType;
import dev.parhamziaei.teahub.enums.dns.DnsRecordType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DnsRecordFilterRequest extends BasePaginationRequest {

    private DnsRecordType type;
    private String zoneName;

}
