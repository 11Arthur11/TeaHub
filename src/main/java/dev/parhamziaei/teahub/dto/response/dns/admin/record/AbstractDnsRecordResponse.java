package dev.parhamziaei.teahub.dto.response.dns.admin.record;

import dev.parhamziaei.teahub.enums.dns.DnsRecordType;
import lombok.Data;

@Data
public abstract class AbstractDnsRecordResponse {

    private String name;
    private DnsRecordType type;


}
