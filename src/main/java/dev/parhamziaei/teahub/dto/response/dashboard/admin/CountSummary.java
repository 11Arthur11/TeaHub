package dev.parhamziaei.teahub.dto.response.dashboard.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountSummary {
    private Long total;
    private Long count;
}