package dev.parhamziaei.teahub.dto.response.dns.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DnsRecordUserResponse {

    private Long id;
    private Long assignedToResourceId;
    private String value;
    private ZoneUserResponse zone;

}
