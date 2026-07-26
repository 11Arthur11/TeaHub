package dev.parhamziaei.teahub.dto.response.dashboard.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResourceOverviewResponse {

    private Long total;
    private Long active;
    private Long suspended;

}
