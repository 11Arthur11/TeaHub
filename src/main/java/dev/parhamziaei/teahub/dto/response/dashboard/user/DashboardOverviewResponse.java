package dev.parhamziaei.teahub.dto.response.dashboard.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardOverviewResponse {

    private ResourceOverviewResponse resourceMetric;
    private Long openTickets;

}
