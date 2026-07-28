package dev.parhamziaei.teahub.dto.response.dashboard.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceMetric {
    private Long total;
    private Long active;
    private Long suspended;
    private Long deploying;
}