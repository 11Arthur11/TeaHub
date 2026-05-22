package dev.parhamziaei.teahub.dto.response.dns.admin;

import dev.parhamziaei.teahub.entity.jpa.dns.BaseDnsProvider;
import dev.parhamziaei.teahub.enums.dns.ZoneStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DnsZoneListResponse {

    private String name;

    private Boolean active;

    private String status;

}
