package dev.parhamziaei.teahub.dto.request.query;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BasePaginationRequest {
    protected int page = 0;
    protected int size = 20;
}
