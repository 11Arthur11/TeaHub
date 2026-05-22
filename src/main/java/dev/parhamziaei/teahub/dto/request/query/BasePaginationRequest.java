package dev.parhamziaei.teahub.dto.request.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BasePaginationRequest {
    @Schema(example = "0")
    protected int page = 0;
    @Schema(example = "20")
    protected int size = 20;
}
