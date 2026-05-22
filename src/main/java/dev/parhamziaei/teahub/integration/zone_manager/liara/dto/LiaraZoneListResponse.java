package dev.parhamziaei.teahub.integration.zone_manager.liara.dto;

import dev.parhamziaei.teahub.enums.dns.ZoneStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LiaraZoneListResponse {

    private String name;

    private ZoneStatus status;

}

